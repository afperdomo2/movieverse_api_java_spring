# AGENTS.md

API REST de películas y autores con **Spring Boot 3.5.4** y **Java 21**, CRUD y sugerencias mediante **Spring AI/OpenAI**.

## Documentación

- [README.md](README.md): instalación, ejecución, configuración y solución de problemas.
- [ARCHITECTURE.md](ARCHITECTURE.md): capas, flujos, modelo de datos, integración con IA y limitaciones.
- Antes de cambiar la estructura del proyecto, lee `ARCHITECTURE.md` y respeta las fronteras entre capas.

## Compilación y ejecución

- **JDK 21 obligatorio.** El wrapper usa Gradle 8.14.3; si Gradle arranca con un JDK incompatible, falla con `Unsupported class file major version 69`.
- En VS Code, `.vscode/settings.json` (ignorado por Git) fija el JDK 21 para este workspace. En una terminal normal, exporta `JAVA_HOME` o usa el setup de [README.md](README.md#java-21-windows).
- En PowerShell usa `.\gradlew.bat <tarea>`; en Linux/macOS usa `./gradlew <tarea>`.
- **Desarrollo:** `.\gradlew.bat bootRun` arranca el perfil `dev` en `http://localhost:8082/movieverse/api`.
- **Swagger:** `http://localhost:8082/movieverse/api/swagger-ui/index.html`.
- **PostgreSQL local:** `docker compose up -d` levanta solo PostgreSQL 16 en el puerto `5434` (`movieverse` / `postgres` / `123456`). El compose no inicia la aplicación.
- `spring-boot-docker-compose` está en el classpath de desarrollo, así que `bootRun` gestiona los servicios del compose; Docker debe estar en ejecución.
- **`build` y `test` no necesitan PostgreSQL**: por defecto solo se ejecutan pruebas unitarias. `integrationTest` sí requiere la base levantada.

## Tests

- **Unitarios (por defecto):** JUnit 5 + Mockito + AssertJ; sin contexto Spring, sin PostgreSQL y sin llamadas a OpenAI. Cubren servicios, repositorios, mappers, controladores, `RestExceptionHandler`, validación de DTOs, películas, autores y `MovieverseAiService`.
  ```powershell
  .\gradlew.bat test
  ```
- **Integración:** `MovieverseApplicationTests.contextLoads` está anotado con `@Tag("integration")` y se ejecuta aparte:
  ```powershell
  docker compose up -d
  .\gradlew.bat integrationTest
  ```
- Test individual:
  ```powershell
  .\gradlew.bat test --tests "com.afperdomo2.movieverse.domain.service.MovieServiceTest"
  ```
- Verificación rápida sin compilar tests: `.\gradlew.bat compileJava`.
- Si MapStruct no refleja cambios, ejecuta `.\gradlew.bat clean` y recompila.
- No añadas `@SpringBootTest` a los tests unitarios; si un test necesita contexto o base de datos, etiquétalo con `@Tag("integration")`.

## Arquitectura

- Flujo: `web/controller` → `domain/service` → interfaz `domain/repository` ← `persistence/repository` → `persistence/crud` → `persistence/entity`.
- `domain/` no debe importar `persistence/` ni `web/`.
- DTOs de entidad en `domain/dto`; paginación compartida en `common/pagination`; utilidades transversales como UUID en `common/uuid`; mappers MapStruct en `persistence/mapper`.
- `common/` no debe importar `domain/`, `persistence/` ni `web/`.
- Para añadir un campo u operación CRUD: DTO, interfaz de repositorio (`MovieRepository` o `AuthorRepository`), implementación en `persistence/repository`, mapper correspondiente y controlador (`MovieController` o `AuthorController`).
- `GET /movies` es paginado (`page` 1-based por defecto 1, `limit` por defecto 10 y máximo 100) y devuelve `PageResponse<MovieDto>`; `MovieService.findAll()` sigue sin paginar para la herramienta de IA.
- La lógica de persistencia vive en `MovieEntityRepository`: ID inexistente → 404; título duplicado → 409; `create` deja `is_active=true`.
- Las PK de `movies` y `authors` son **UUID v7** (`java.util.UUID`) generadas en la aplicación con `UuidV7Generator.generate()` (`common/uuid`), que delega en `uuid-creator`; no uses `@GeneratedValue` ni `uuidv7()` de PostgreSQL (no existe en PostgreSQL 16).
- `CreateMovieDto` no incluye `id` ni `isActive`: el ID lo genera el repositorio y deja `is_active=true`. El género es el enum `Genre`; valida título ≤ 255 y duración ≤ 600 minutos.
- `PATCH /movies/{id}` usa `PatchMovieDto` con `Optional`: puede actualizar título, fecha, rating, `genre` e `isActive`; body vacío → 400, título duplicado → 409, género inválido → 400 y la operación es `@Transactional`.
- `RestExceptionHandler` traduce las excepciones de dominio, `ObjectOptimisticLockingFailureException` → 409 y `MethodArgumentTypeMismatchException` (UUID inválido en la ruta) → 400.
- **Autores:** `AuthorEntity` se mapea a `authors` con PK `UUID`. `GET /authors` es paginado y ordena por `lastName` y `name`. `name` y `lastName` son obligatorios y su combinación es única → 409. `nationality`, `birthDate` y `biography` son opcionales. `POST` fuerza `is_active=true`; solo `PATCH` permite cambiar `isActive`. No hay relación con `movies` todavía.
- La lógica de autores vive en `AuthorEntityRepository`; el flujo es `AuthorController` → `AuthorService` → `AuthorRepository` ← `AuthorEntityRepository`.
- `RatingMapper` calcula `ratingString`; los umbrales están en esa clase.
- Las entidades extienden `AuditableEntity` (`persistence/entity`), que a su vez extiende `BaseEntity`. `BaseEntity` aporta `id` UUID y `version`; `AuditableEntity` aporta `createdAt`/`updatedAt` con `@CreatedDate`/`@LastModifiedDate`. `@EnableJpaAuditing` vive en `config/JpaAuditingConfig`. Los campos heredados no se redeclaran en las entidades.
- `version` es un `@Version` para bloqueo optimista y vive en `BaseEntity`; las columnas de auditoría usan `NOT NULL DEFAULT now()` para sobrevivir a `ddl-auto=update`.
- `data.sql` inserta películas y autores de ejemplo solo si su tabla está vacía, y se ejecuta en cada arranque de `dev`. Los seeds usan UUID v7 literales con cast `::uuid`.

Detalle completo en [ARCHITECTURE.md](ARCHITECTURE.md).

## Integración con IA

- `MovieverseAiService` es una clase `@Service` que encapsula un `ChatClient`; `MovieService.findAll` se expone con `@Tool` y se registra por petición con `.tools(movieService)`.
- `GET /hello` y `POST /movies/suggestion` usan OpenAI `gpt-4o-mini` (configurable con `spring.ai.openai.chat.options.model`).
- La clave por defecto es `demo`; sin una clave real esos endpoints fallan. Usa `OPENAI_API_KEY`.
- `OPENAI_BASE_URL` y `OPENAI_MODEL` permiten cambiar de proveedor sin tocar archivos. `config/AiHttpConfig` añade `x-opencode-session` cuando el base-url es de OpenCode Go.
- Si usas un endpoint compatible con OpenAI (por ejemplo OpenCode Go), configura `spring.ai.openai.base-url`; recuerda que Spring AI añade `/v1/chat/completions`.
- No acoples el CRUD a la disponibilidad de la IA.

## Precauciones

- No ejecutes el perfil `prod` en local sin revisar la configuración de base de datos externa.
- No agregues secretos a archivos versionados; usa `SPRING_DATASOURCE_*` y `OPENAI_API_KEY`.
- El contenedor Docker usa el perfil `prod` y escucha en `8080`; el desarrollo local usa `8082`.
- Si cambias el tipo de una PK existente (por ejemplo `BIGINT` → `UUID`), `ddl-auto=update` no la convierte: respalda y elimina las tablas para que Hibernate las recree.
