# 🎬 Movieverse API

Una API REST para la gestión de películas construida con **Spring Boot 3.5.4** y **Java 21**. Esta aplicación incluye integración con inteligencia artificial usando **LangChain4j** para generar sugerencias personalizadas de películas.

## 🚀 Características

- ✅ **CRUD completo de películas** (Crear, Leer, Actualizar, Eliminar)
- 🤖 **Integración con IA** para sugerencias personalizadas usando LangChain4j y OpenAI
- 📚 **Documentación automática** con Swagger/OpenAPI 3
- 🗄️ **Base de datos PostgreSQL** con Docker Compose
- 🔧 **Mapeo automático** con MapStruct
- ✅ **Validaciones** con Bean Validation
- 🏗️ **Arquitectura hexagonal** (Domain-driven design)
- 🐳 **Containerización** con Docker
- 🔄 **Hot reload** en desarrollo con DevTools

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Web** - REST API
- **Spring Data JPA** - Persistencia de datos
- **Spring Validation** - Validación de datos
- **PostgreSQL** - Base de datos
- **LangChain4j** - Integración con IA
- **OpenAI GPT-4o-mini** - Modelo de IA
- **MapStruct 1.6.3** - Mapeo de objetos
- **SpringDoc OpenAPI** - Documentación automática
- **Docker Compose** - Orchestración de contenedores
- **Gradle** - Gestión de dependencias

## 📋 Prerequisitos

- **Java 21** o superior
- **Docker** y **Docker Compose**
- **Gradle** (incluido wrapper)

## 🚀 Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/afperdomo2/movieverse_api_java_spring.git
cd movieverse
```

### 2. Configurar la base de datos

Ejecutar PostgreSQL con Docker Compose:

```bash
docker-compose up -d
```

Esto creará una base de datos PostgreSQL en el puerto `5434` con:

- **Base de datos**: `movieverse`
- **Usuario**: `postgres`
- **Contraseña**: `123456`

### 3. Limpiar y construir el proyecto

Si tienes problemas con dependencias en VS Code, ejecuta estos comandos:

```bash
# Limpiar proyecto
./gradlew clean

# Construir proyecto
./gradlew build
```

### 4. Ejecutar la aplicación

```bash
./gradlew bootRun
```

La aplicación estará disponible en: `http://localhost:8081/movieverse/api`

## 📊 Datos de Prueba

La aplicación incluye datos iniciales de películas populares que se cargan automáticamente al iniciar. Incluye películas como:

- The Shawshank Redemption
- The Godfather
- The Dark Knight
- Pulp Fiction
- Inception
- Y muchas más...

## 📖 Documentación de la API

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de Swagger en:

```url
http://localhost:8081/movieverse/api/swagger-ui/index.html
```

## 🎯 Endpoints Principales

### Películas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/movies` | Obtener todas las películas |
| `GET` | `/movies/{id}` | Obtener película por ID |
| `POST` | `/movies` | Crear nueva película |
| `PUT` | `/movies/{id}` | Actualizar película |
| `DELETE` | `/movies/{id}` | Eliminar película |
| `POST` | `/movies/suggestion` | Generar sugerencias con IA |

### Pruebas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/hello` | Saludo generado con IA |

## 🧠 Funcionalidad de IA

La aplicación incluye dos funcionalidades principales de IA:

### 1. Saludo Personalizado

Genera un saludo de bienvenida personalizado para la plataforma.

### 2. Sugerencias de Películas

Basado en las preferencias del usuario, la IA recomienda hasta 3 películas de la base de datos.

**Ejemplo de solicitud:**

```json
{
  "userPreferences": "Me gustan las películas de ciencia ficción y acción con efectos especiales"
}
```

## 🏗️ Arquitectura del Proyecto

```text
src/main/java/com/afperdomo2/movieverse/
├── 📁 domain/                 # Lógica de negocio
│   ├── 📁 dto/               # Data Transfer Objects
│   ├── 📁 repository/        # Interfaces de repositorio
│   └── 📁 service/           # Servicios de dominio
├── 📁 persistence/           # Capa de persistencia
│   ├── 📁 crud/             # Repositorios CRUD
│   ├── 📁 entity/           # Entidades JPA
│   ├── 📁 mapper/           # Mappers MapStruct
│   └── 📁 repository/       # Implementaciones de repositorio
└── 📁 web/                  # Capa de presentación
    └── 📁 controller/       # Controladores REST
```

## 🎭 DTOs Principales

### MovieDto

Representa una película completa con todos sus datos.

### CreateMovieDto

DTO para crear nuevas películas con validaciones:

- Título obligatorio
- Duración mayor a 0
- Fecha de lanzamiento no futura
- Calificación entre 0 y 9

### UpdateMovieDto

DTO para actualizar películas existentes (título, fecha y calificación).

### SuggestionRequestDto

DTO para solicitudes de sugerencias de IA.

## 🗄️ Modelo de Base de Datos

### Tabla: movies

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | BIGINT | Identificador único (autoincremental) |
| `title` | VARCHAR | Título de la película (único) |
| `duration` | INTEGER | Duración en minutos |
| `genre` | VARCHAR(50) | Género de la película |
| `release_date` | DATE | Fecha de lanzamiento |
| `rating` | DECIMAL(3,2) | Calificación (0.00 - 9.99) |
| `status` | VARCHAR(1) | Estado ('A'=Activo, 'D'=Disponible) |

## ⚙️ Configuración

### Perfiles de Aplicación

- **dev** (por defecto): Configuración de desarrollo
- **prod**: Configuración de producción

### Variables de Entorno Importantes

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5434/movieverse
spring.datasource.username=postgres
spring.datasource.password=123456

# IA (LangChain4j)
langchain4j.open-ai.chat-model.api-key=demo
langchain4j.open-ai.chat-model.model-name=gpt-4o-mini

# Servidor
server.port=8081
server.servlet.context-path=/movieverse/api
```

## 🧪 Testing

Para ejecutar las pruebas:

```bash
./gradlew test
```

## 🚀 Despliegue

### Construcción para Producción

```bash
./gradlew clean build -Pprod
```

### Generar JAR

```bash
./gradlew bootJar
```

El archivo JAR se generará en: `build/libs/movieverse-0.0.1-SNAPSHOT.jar`

## 🔧 Comandos Útiles de Gradle

```bash
# Limpiar proyecto (útil cuando hay problemas con dependencias en VS Code)
./gradlew clean

# Construir proyecto
./gradlew build

# Ejecutar aplicación
./gradlew bootRun

# Ejecutar tests
./gradlew test

# Generar JAR ejecutable
./gradlew bootJar

# Ver dependencias
./gradlew dependencies

# Verificar actualizaciones de dependencias
./gradlew dependencyUpdates
```

## 🐛 Solución de Problemas

### Problemas con Dependencias en VS Code

Si tienes problemas con las dependencias en VS Code, ejecuta:

```bash
./gradlew clean
./gradlew build
```

Luego reinicia VS Code o recarga la ventana (`Ctrl+Shift+P` > "Developer: Reload Window").

### Error de Conexión a Base de Datos

Asegúrate de que PostgreSQL esté ejecutándose:

```bash
docker-compose ps
```

Si no está activo:

```bash
docker-compose up -d
```

### Problemas con la API Key de OpenAI

La aplicación usa una API key de demo. Para usar tu propia key:

1. Obtén una API key de OpenAI
2. Modifica `application.properties`:

```properties
langchain4j.open-ai.chat-model.api-key=tu_api_key_aqui
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

### Andrés Felipe Perdomo

- GitHub: [@afperdomo2](https://github.com/afperdomo2)
- Email: <afperdomo2@gmail.com>

---

⭐ Si este proyecto te fue útil, ¡no olvides darle una estrella!
