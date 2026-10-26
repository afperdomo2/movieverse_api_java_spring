# 🎬 Movieverse API

![Java 21](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1)
![License](https://img.shields.io/badge/License-MIT-yellow)

API REST para la gestión de películas, construida con **Spring Boot 3.5.4** y **Java 21**. Incluye CRUD, validaciones, PostgreSQL, documentación Swagger y sugerencias personalizadas con **Spring AI/OpenAI**.

> 🏛️ Arquitectura, capas y decisiones técnicas: [ARCHITECTURE.md](ARCHITECTURE.md)

## 🧭 Contenido

**🚀 Primeros pasos**

- [1. 🎬 Descripción general](#descripcion-general)
- [2. 🛠️ Stack tecnológico](#stack-tecnologico)
- [3. 📋 Prerrequisitos](#prerrequisitos)
- [4. 🚀 Inicio rápido](#inicio-rapido)
- [5. ☕ Java 21 en Windows 11](#java-21-windows)

**🛠️ Uso y operación**

- [6. 🎯 API](#api)
- [7. ⚙️ Configuración](#configuracion)
- [8. 🧪 Testing](#testing)
- [9. 📦 Despliegue](#despliegue)
- [10. 🐛 Solución de problemas](#solucion-problemas)

**📦 Proyecto**

- [11. 📚 Documentación adicional](#documentacion-adicional)
- [12. 🤝 Contribuciones](#contribuciones)
- [13. 📄 Licencia](#licencia)
- [🏛️ Arquitectura detallada](ARCHITECTURE.md)

<a id="descripcion-general"></a>
## 1. 🎬 Descripción general

Movieverse API expone un catálogo de películas y permite crear, consultar, actualizar y eliminar registros. La aplicación usa una arquitectura hexagonal: los controladores REST dependen de servicios de dominio, y la persistencia se implementa detrás de una interfaz de repositorio.

**✨ Características principales**

- CRUD completo de películas.
- Validación de entrada con Bean Validation.
- Persistencia en PostgreSQL con Spring Data JPA.
- Mapeo entre entidades y DTOs con MapStruct.
- Documentación interactiva con Swagger/OpenAPI.
- Sugerencias de películas con Spring AI y OpenAI.
- Datos de ejemplo cargados automáticamente en desarrollo.
- Docker Compose para la base de datos local.

<a id="stack-tecnologico"></a>
## 2. 🛠️ Stack tecnológico

| Componente | Uso |
|---|---|
| Java 21 | Lenguaje y runtime |
| Spring Boot 3.5.4 | Aplicación y autoconfiguración |
| Spring Web | API REST |
| Spring Data JPA | Persistencia |
| Spring Validation | Validaciones |
| PostgreSQL 16 | Base de datos |
| Spring AI 1.1.8 | Integración con IA |
| OpenAI `gpt-4o-mini` | Modelo de sugerencias |
| MapStruct 1.6.3 | Mapeo entidad/DTO |
| Lombok 1.18.38 | Getters/setters de entidades |
| SpringDoc OpenAPI 2.8.9 | Swagger UI |
| Gradle 8.14.3 | Build y dependencias |
| Docker Compose | PostgreSQL local |

<a id="prerrequisitos"></a>
## 3. 📋 Prerrequisitos

- **JDK 21** para compilar y ejecutar. Gradle 8.14.3 requiere una JVM compatible.
- **Docker Desktop** con Docker Compose.
- **Gradle Wrapper** incluido en el repositorio; no hace falta instalar Gradle.
- Opcional: VS Code con **Extension Pack for Java** y **Spring Boot Extension Pack**.

Si usas Windows 11 y no tienes JDK 21, revisa la [guía de instalación](#java-21-windows). En macOS/Linux instala un JDK 21 con tu gestor de paquetes.

<a id="inicio-rapido"></a>
## 4. 🚀 Inicio rápido

### 4.1 📥 Clonar el repositorio

```bash
git clone https://github.com/afperdomo2/movieverse_api_java_spring.git
cd movieverse_api_java_spring
```

### 4.2 🐘 Levantar PostgreSQL

```bash
docker compose up -d
```

La base local queda disponible en el puerto `5434`:

| Dato | Valor |
|---|---|
| Base de datos | `movieverse` |
| Usuario | `postgres` |
| Contraseña | `123456` |

### 4.3 🏗️ Compilar y ejecutar tests

**🪟 Windows (PowerShell):**

```powershell
.\gradlew.bat clean
.\gradlew.bat build
```

**🐧 Linux/macOS:**

```bash
./gradlew clean
./gradlew build
```

> `build` ejecuta los tests unitarios y no necesita PostgreSQL. Los tests de integración se ejecutan aparte con `integrationTest` (ver [sección 8](#testing)).

### 4.4 ▶️ Ejecutar la aplicación

**🪟 Windows (PowerShell):**

```powershell
.\gradlew.bat bootRun
```

**🐧 Linux/macOS:**

```bash
./gradlew bootRun
```

La aplicación usa el perfil `dev` y queda disponible en:

- API: `http://localhost:8082/movieverse/api`
- Swagger: `http://localhost:8082/movieverse/api/swagger-ui/index.html`

<a id="java-21-windows"></a>
## 5. ☕ Java 21 en Windows 11 (opcional)

Esta sección solo aplica si trabajas en Windows 11 y no tienes JDK 21. El proyecto puede convivir con otras versiones de Java; la configuración de esta guía es **solo para este repositorio**.

### 5.1 📥 Instalar Eclipse Temurin 21

```powershell
winget install --id EclipseAdoptium.Temurin.21.JDK --exact
```

La instalación queda por defecto en una ruta similar a:

```text
C:\Program Files\Eclipse Adoptium\jdk-21.0.x-hotspot
```

Puedes ver la carpeta exacta con:

```powershell
Get-ChildItem "C:\Program Files\Eclipse Adoptium"
```

> Algunos instaladores configuran `JAVA_HOME` globalmente. Si eso ocurre y no quieres afectar a otros proyectos, restaura el valor que tenías antes de instalar el JDK 21.

### 5.2 ⚙️ Configurar Java 21 solo para este repositorio

**Opción A — 💻 Terminal de PowerShell**

Cada vez que abras una terminal para trabajar en este proyecto:

```powershell
# Reemplaza la ruta por tu carpeta real de Temurin 21
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.x-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

Esto solo afecta a esa terminal; no cambia la configuración global de Java.

**Opción B — 🧩 VS Code**

Crea `.vscode/settings.json` en la raíz del proyecto. Esa carpeta está en `.gitignore`, así que la configuración no se sube y solo aplica a este workspace:

```json
{
  "java.jdt.ls.java.home": "C:/Program Files/Eclipse Adoptium/jdk-21.0.x-hotspot",
  "java.import.gradle.java.home": "C:/Program Files/Eclipse Adoptium/jdk-21.0.x-hotspot",
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-21",
      "path": "C:/Program Files/Eclipse Adoptium/jdk-21.0.x-hotspot",
      "default": true
    }
  ]
}
```

Ajusta la ruta si tu versión es distinta y recarga VS Code con `Ctrl+Shift+P` → **Developer: Reload Window**.

> **No** configures `JAVA_HOME` al JDK 21 a nivel `"User"` o `"Machine"`: afectaría a todos los proyectos. La configuración del JDK 21 debe quedar limitada a este repositorio.

### 5.3 ✅ Verificar

```powershell
.\gradlew.bat --version
```

Debe indicar `Launcher JVM: 21...`. En la Opción B, `java -version` puede seguir mostrando otra versión porque el `PATH` del sistema no cambia; lo importante es que Gradle use el JDK 21.

<a id="api"></a>
## 6. 🎯 API

La documentación interactiva está disponible en Swagger:

```text
http://localhost:8082/movieverse/api/swagger-ui/index.html
```

Las rutas son relativas al contexto `/movieverse/api`:

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/movies` | Listar películas |
| `GET` | `/movies/{id}` | Obtener una película |
| `POST` | `/movies` | Crear una película |
| `PUT` | `/movies/{id}` | Actualizar una película |
| `DELETE` | `/movies/{id}` | Eliminar una película |
| `POST` | `/movies/suggestion` | Generar sugerencias con IA |
| `GET` | `/hello` | Saludo generado con IA |

Ejemplo de sugerencia:

```json
{
  "userPreferences": "Me gustan la ciencia ficción y la acción con efectos especiales"
}
```

<a id="configuracion"></a>
## 7. ⚙️ Configuración

### 7.1 🧩 Perfiles

- `dev` (por defecto): PostgreSQL local en `5434`, puerto `8082`, `ddl-auto=update` y carga de `data.sql`.
- `prod`: puerto `8080` y configuración de base de datos externa.

### 7.2 🔑 Variables y propiedades relevantes

| Propiedad / variable | Uso |
|---|---|
| `server.port` | `8081` en la configuración base; `dev` lo sobrescribe a `8082` y `prod` a `8080` |
| `spring.datasource.url` | Conexión JDBC a PostgreSQL |
| `spring.datasource.username` / `password` | Credenciales de la base |
| `spring.jpa.hibernate.ddl-auto` | Actualización automática del esquema (`update` en dev/prod) |
| `spring.ai.openai.api-key` | API key de OpenAI (o compatible); por defecto `demo` |
| `spring.ai.openai.chat.options.model` | Modelo de chat; por defecto `gpt-4o-mini` |
| `spring.ai.openai.base-url` | Endpoint compatible con OpenAI; por defecto `https://api.openai.com` |
| `OPENAI_API_KEY` | Variable de entorno para una key real |
| `OPENAI_BASE_URL` | Variable opcional para cambiar de proveedor (por ejemplo OpenCode Go) |
| `OPENAI_MODEL` | Variable opcional para elegir el modelo |
| `OPENCODE_SESSION` | Cabecera `x-opencode-session` que exige OpenCode Go; por defecto `movieverse-api` |

Para usar OpenCode Go, define las tres variables antes de arrancar:

```powershell
$env:OPENAI_API_KEY = "tu_api_key"
$env:OPENAI_BASE_URL = "https://opencode.ai/inference/go/openai"
$env:OPENAI_MODEL = "deepseek-v4.1-flash"
.\gradlew.bat bootRun
```

No agregues claves ni credenciales reales a archivos versionados. Usa variables de entorno.

<a id="testing"></a>
## 8. 🧪 Testing

Los tests están separados en dos grupos:

- **Unitarios** (por defecto): JUnit 5, Mockito y AssertJ. No levantan el contexto de Spring, no usan PostgreSQL y no llaman a OpenAI.
- **Integración** (`@Tag("integration")`): levantan el contexto completo y requieren PostgreSQL en `5434`.

**Unitarios (sin base de datos):**

```powershell
.\gradlew.bat test
```

**Integración (requiere PostgreSQL):**

```powershell
docker compose up -d
.\gradlew.bat integrationTest
```

Ejecutar un único test unitario:

```powershell
.\gradlew.bat test --tests "com.afperdomo2.movieverse.domain.service.MovieServiceTest"
```

> `build` solo ejecuta los tests unitarios. Para una verificación rápida sin compilar tests: `.\gradlew.bat compileJava`.

<a id="despliegue"></a>
## 9. 📦 Despliegue

### 9.1 📦 Generar el JAR

```powershell
.\gradlew.bat clean build
.\gradlew.bat bootJar
```

El JAR se genera en `build/libs/movieverse-1.0.0.jar`.

### 9.2 ▶️ Ejecutar el JAR

**🪟 Windows (PowerShell):**

```powershell
java "-Dspring.profiles.active=prod" -jar build/libs/movieverse-1.0.0.jar
```

**🐧 Linux/macOS:**

```bash
java -Dspring.profiles.active=prod -jar build/libs/movieverse-1.0.0.jar
```

### 9.3 🐳 Docker

```bash
docker build -t movieverse-api .

docker run -d \
  --name movieverse-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5434/movieverse \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  -e OPENAI_API_KEY=tu_api_key \
  movieverse-api
```

El `docker-compose.yaml` del repositorio **solo levanta PostgreSQL**; no inicia la aplicación. Para desarrollo local usa `bootRun`; para producción construye la imagen con el `Dockerfile`.

<a id="solucion-problemas"></a>
## 10. 🐛 Solución de problemas

### 10.1 ❌ Error `Unsupported class file major version 69`

Gradle está usando una versión de Java distinta de 21. Verifica:

```powershell
.\gradlew.bat --version
```

Debe mostrar `Launcher JVM: 21...`. Si no, configura `JAVA_HOME` con un JDK 21 o revisa la [guía de Windows 11](#java-21-windows).

### 10.2 🐘 No conecta con PostgreSQL

Comprueba el contenedor:

```bash
docker compose ps
docker compose up -d
```

La aplicación espera PostgreSQL en `localhost:5434`.

### 10.3 🤖 La IA devuelve error 500

Los endpoints `/hello` y `/movies/suggestion` necesitan una API key real. Configúrala antes de arrancar:

```powershell
$env:OPENAI_API_KEY = "tu_api_key"
.\gradlew.bat bootRun
```

El CRUD funciona sin clave de IA.

### 10.4 🧰 Problemas con dependencias en VS Code

```powershell
.\gradlew.bat clean
.\gradlew.bat build
```

Luego recarga VS Code con `Ctrl+Shift+P` → **Developer: Reload Window**. Si persiste, ejecuta **Java: Clean Java Language Server Workspace**.

<a id="documentacion-adicional"></a>
## 11. 📚 Documentación adicional

- [ARCHITECTURE.md](ARCHITECTURE.md): capas, flujos, modelo de datos, integración con IA y decisiones técnicas.
- [AGENTS.md](AGENTS.md): instrucciones para agentes de código que trabajen en el repositorio.
- Swagger UI: `http://localhost:8082/movieverse/api/swagger-ui/index.html`.

<a id="contribuciones"></a>
## 12. 🤝 Contribuciones

Las contribuciones son bienvenidas. Abre un issue o un pull request describiendo el cambio. Para cambios grandes, crea una rama desde `main`.

<a id="licencia"></a>
## 13. 📄 Licencia

Este proyecto está bajo la licencia MIT.
