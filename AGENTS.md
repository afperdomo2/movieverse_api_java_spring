# AGENTS.md

API REST de películas con **Spring Boot 3.5.4** y **Java 21**, CRUD y sugerencias mediante **Spring AI/OpenAI**.

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

- **Unitarios (por defecto):** JUnit 5 + Mockito + AssertJ; sin contexto Spring, sin PostgreSQL y sin llamadas a OpenAI. Cubren servicios, repositorio, mappers, controladores, `RestExceptionHandler`, validación de DTOs y `MovieverseAiService`.
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
- DTOs en `domain/dto`; mappers MapStruct en `persistence/mapper`.
- Para añadir un campo u operación CRUD: DTO, `MovieRepository`, `MovieEntityRepository`, `MovieMapper` y `MovieController`.
- La lógica de persistencia vive en `MovieEntityRepository`: ID inexistente → 404; título duplicado → 409; `create` fuerza `status="D"`.
- `RestExceptionHandler` traduce las excepciones de dominio; los null checks de los controladores son código muerto.
- `RatingMapper` calcula `ratingString`; los umbrales están en esa clase.
- `data.sql` solo inserta si `movies` está vacía y se ejecuta en cada arranque de `dev`.

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
