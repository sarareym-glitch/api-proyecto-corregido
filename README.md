# API REST – Productos y Usuarios (Spring Boot)

API REST desarrollada con **Spring Boot** que expone el CRUD completo de **Productos** y **Usuarios** con arquitectura por capas (controlador → servicio → repositorio → entidad), persistencia en **MySQL** con Spring Data JPA y reducción de código repetitivo con **Lombok**.

Actividad: *Guía de aprendizaje – API REST con Spring Boot (Productos y Usuarios)*.

---

## 1. Tecnologías

| Tecnología | Versión | Fuente |
|---|---|---|
| Java (JDK) | 25 (`java.version` del `pom.xml`) | `pom.xml` |
| Spring Boot | 4.1.1 | `pom.xml` (parent) |
| Spring Web MVC / Data JPA / Validation | gestionadas por Spring Boot | `pom.xml` |
| Lombok | 1.18.46 (la usa IntelliJ en `.idea/compiler.xml`; en Maven la versión la gestiona Spring Boot) | `pom.xml` |
| Base de datos | MySQL 8.x (driver `mysql-connector-j`) | `pom.xml` |
| Cifrado de contraseñas | BCrypt (`spring-security-crypto`) | `pom.xml` |
| Maven | 3.9.x (incluye wrapper `mvnw`) | `.mvn/wrapper` |

> Nota: la guía menciona "JDK 17 o superior"; el proyecto está configurado con JDK 25, que cumple "o superior".

## 2. Requisitos previos

- JDK 25 instalado (`java -version`).
- MySQL 8 en `localhost:3306` **o** Docker Desktop (se incluye `docker-compose.yml`).
- IntelliJ IDEA o Visual Studio Code (con *Extension Pack for Java* y *Spring Boot Extension Pack*).
- Lombok habilitado en el IDE (ver sección 8).
- Postman.

## 3. Base de datos

### Opción A – Docker (recomendada)

```powershell
docker compose up -d
```

Crea un MySQL con la base `tiendaya`, usuario `tiendaya` y contraseña `tiendaya123` (**credenciales de ejemplo, solo para desarrollo**).

### Opción B – MySQL instalado

Con la base de datos y usuario que prefiera. La URL incluye `createDatabaseIfNotExist=true`, así que la base `tiendaya` se crea sola si el usuario tiene permisos. Ejemplo de creación manual del usuario:

```sql
CREATE USER 'tiendaya'@'localhost' IDENTIFIED BY 'tiendaya123';
GRANT ALL PRIVILEGES ON tiendaya.* TO 'tiendaya'@'localhost';
FLUSH PRIVILEGES;
```

### `src/main/resources/application.properties`

```properties
spring.application.name=api
server.port=8001

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/tiendaya?createDatabaseIfNotExist=true}
spring.datasource.username=${DB_USERNAME:tiendaya}        # credencial de EJEMPLO
spring.datasource.password=${DB_PASSWORD:tiendaya123}     # credencial de EJEMPLO

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Los valores por defecto pueden sobrescribirse con las variables de entorno `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`. Con `ddl-auto=update`, Hibernate crea/actualiza las tablas `productos` y `usuarios` al iniciar.

## 4. Compilar y ejecutar (Windows – PowerShell)

```powershell
cd ruta\al\proyecto\api
.\mvnw.cmd clean install        # compila y ejecuta las pruebas
.\mvnw.cmd spring-boot:run      # arranca la API
```

> `mvn clean install` ejecuta `ApiApplicationTests`, que **necesita MySQL encendido**. Para compilar sin base de datos: `.\mvnw.cmd clean install -DskipTests`.

Debe aparecer en consola: `Tomcat started on port 8001` y `Started ApiApplication`. La API queda en `http://localhost:8001`.

## 5. Endpoints

| Método | Ruta | Descripción | Códigos |
|---|---|---|---|
| GET | `/api/productos` | Listar productos | 200 |
| GET | `/api/productos/{id}` | Consultar producto | 200 / 404 |
| POST | `/api/productos` | Crear producto | 201 / 400 |
| PUT | `/api/productos/{id}` | Actualizar producto | 200 / 400 / 404 |
| DELETE | `/api/productos/{id}` | Eliminar producto | 204 / 404 |
| GET | `/api/usuarios` | Listar usuarios | 200 |
| GET | `/api/usuarios/{id}` | Consultar usuario | 200 / 404 |
| POST | `/api/usuarios` | Crear usuario | 201 / 400 |
| PUT | `/api/usuarios/{id}` | Actualizar usuario | 200 / 400 / 404 |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario | 204 / 404 |

### Productos – ejemplos

**POST / PUT** `/api/productos` — cuerpo:

```json
{
  "name": "Teclado mecánico",
  "description": "Switches rojos, retroiluminado",
  "price": 189900,
  "stock": 15
}
```

Respuesta (201 en POST, 200 en PUT / GET):

```json
{
  "id": 1,
  "name": "Teclado mecánico",
  "description": "Switches rojos, retroiluminado",
  "price": 189900.0,
  "stock": 15
}
```

`GET /api/productos` devuelve un arreglo con objetos de esa forma. `DELETE` responde `204` sin cuerpo.

Validaciones: `name` obligatorio (máx. 100), `price` obligatorio y ≥ 0, `stock` ≥ 0.

### Usuarios – ejemplos

**POST** `/api/usuarios` — cuerpo (la contraseña es obligatoria al crear):

```json
{
  "name": "Ana Pérez",
  "email": "ana@example.com",
  "password": "Secreto123",
  "role": "USER"
}
```

Respuesta `201` (**nunca incluye la contraseña**):

```json
{
  "id": 1,
  "name": "Ana Pérez",
  "email": "ana@example.com",
  "role": "USER"
}
```

**PUT** `/api/usuarios/{id}` — mismo cuerpo; `password` es opcional (si se omite, se conserva la actual). Un usuario puede conservar su propio correo, pero no usar el de otro.

Reglas de negocio:

- El correo es **único** (se normaliza a minúsculas); se valida al crear y al actualizar.
- La contraseña se guarda con **BCrypt** y no aparece en respuestas, errores ni logs (las respuestas usan `UserResponse`, que no tiene ese campo).
- `role` por defecto: `USER`.

## 6. Ejemplos de error

**404 – recurso inexistente** (`GET /api/productos/999`):

```json
{
  "timestamp": "2026-01-01T10:00:00.000",
  "status": 404,
  "error": "Producto no encontrado con id 999"
}
```

**400 – correo duplicado** (`POST /api/usuarios` con un correo existente):

```json
{
  "timestamp": "2026-01-01T10:00:00.000",
  "status": 400,
  "error": "Ya existe un usuario con el correo ana@example.com"
}
```

**400 – validación** (`POST /api/productos` con precio negativo):

```json
{
  "timestamp": "2026-01-01T10:00:00.000",
  "status": 400,
  "error": "Datos de entrada inválidos",
  "detalles": { "price": "El precio no puede ser negativo" }
}
```

(Las fechas son ilustrativas.)

## 7. Pruebas con Postman

1. Inicie MySQL y la API.
2. En Postman: **Import** → `Coleccion_Postman_API.postman_collection.json`.
3. La variable de colección `baseUrl` es `http://localhost:8001`.
4. Ejecute con **Collection Runner** primero la carpeta **Productos** y luego **Usuarios**, en orden. Las peticiones de creación guardan los `id` en variables (`productoId1`, `usuarioId1`, …), por lo que no dependen de IDs fijos. Cada petición incluye scripts de prueba (código de estado y estructura).
5. Casos manuales: compare el caso 11 con `SELECT * FROM productos;` / `SELECT * FROM usuarios;`; para el caso 12 reinicie la API y ejecute la petición "Persistencia".
6. Tome una captura por caso (método, URL, cuerpo, código y respuesta) para el informe.

Pruebas automáticas Java (no requieren MySQL salvo `ApiApplicationTests`): `ProductServiceTest`, `UserServiceTest`, `UserControllerTest`.

## 8. Lombok en el IDE

- **IntelliJ IDEA**: *Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing*. Plugin Lombok (incluido en versiones recientes).
- **VS Code**: instale *Extension Pack for Java* y *Lombok Annotations Support for VS Code*; después *Java: Clean Java Language Server Workspace*.
- Si ve `cannot find symbol: method getName()`, es Lombok sin procesar anotaciones.

## 9. Estructura

```
src/main/java/com/example/api
├── ApiApplication.java
├── controller   ProductController, UserController
├── service      ProductService, UserService
├── repository   ProductRepository, UserRepository
├── model        Product, ProductRequest, ProductResponse,
│                User, UserRequest, UserResponse
└── utils        ResourceNotFoundException, ProductNotFoundException,
                 UserNotFoundException, GlobalExceptionHandler, SecurityConfig
```

## 10. Autor(es)

- **[Nombre del aprendiz]** – Ficha **[número de ficha]** – **[Institución / Programa]**
- Instructor: **[Nombre del instructor]**
