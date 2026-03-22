# Chakray - Prueba Técnica

REST API de gestión de usuarios construida con **Spring Boot 4**, **JDK 21**, **Maven** y **PostgreSQL**.

---

## Tecnologías

- Java 21
- Spring Boot 4.0.4 (Web MVC, Data JPA, Security, Validation)
- PostgreSQL 16
- JWT (jjwt 0.12.6) para autenticación
- Jasypt (AES-256) para cifrado de contraseñas
- Lombok
- Docker & Docker Compose
- JUnit 5 & Mockito

---

## Requisitos previos

- **JDK 21**
- **Maven 3.6.3+**
- **PostgreSQL 16** (o usar Docker)
- **Docker & Docker Compose** (opcional)

---

## Inicio rápido

### Opción 1 — Docker Compose (recomendado)

```bash
docker-compose up --build
```

Esto levanta PostgreSQL y la aplicación en `http://localhost:8080`.

### Opción 2 — Ejecución local

1. Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE chakraydb;
```

2. Verificar las credenciales en `src/main/resources/application.properties` (por defecto: `postgres` / `postgres`).

3. Compilar y ejecutar:

```bash
./mvnw clean package -DskipTests
java -jar target/chakrayCodeTest-0.0.1-SNAPSHOT.jar
```

La API estará disponible en `http://localhost:8080`.

---

## Datos iniciales

Al arrancar, `DataInitializer` inserta 2 usuarios de prueba si la base de datos está vacía. Las contraseñas se cifran automáticamente con AES-256 (Jasypt) en el arranque.

| Usuario | tax_id        | Password |
|---------|---------------|----------|
| user1   | AARR990101XXX | 123456   |
| user2   | BBRR990202YYY | 123456   |

---

## Colección Postman

Importa la colección para probar todos los endpoints directamente:

[Ver colección en Postman](https://alexlmcode-40ffdd72-2389086.postman.co/workspace/Alejandro-Lujan's-Workspace~7863bb3a-f2a6-4c26-8a5f-b5e7a4f52a5d/collection/47619569-5e737e62-aa5c-4232-a986-0f29fac766f8?action=share&source=copy-link&creator=47619569)

---

## Endpoints

### Autenticación

| Método | Ruta              | Descripción                         | Auth |
|--------|-------------------|-------------------------------------|------|
| POST   | `/api/auth/login` | Iniciar sesión (devuelve JWT token) | No   |

**Body de login:**

```json
{
  "username": "AARR990101XXX",
  "password": "123456"
}
```

**Respuesta:**

```json
{
  "token": "<JWT_TOKEN>"
}
```

### Usuarios

| Método | Ruta                | Descripción                           | Auth |
|--------|---------------------|---------------------------------------|------|
| GET    | `/api/users`        | Listar usuarios (con sort y filtros)  | Sí   |
| POST   | `/api/users`        | Crear usuario                         | No   |
| PATCH  | `/api/users/{id}`   | Actualizar usuario por ID             | Sí   |
| DELETE | `/api/users/{id}`   | Eliminar usuario por ID               | Sí   |

### Ordenamiento

```
GET /api/users?sortedBy=name
```

Campos válidos: `email`, `id`, `name`, `phone`, `tax_id`, `created_at`.

### Filtrado

Formato: `filter={campo}+{operador}+{valor}`

| Operador | Significado   |
|----------|---------------|
| `co`     | Contiene      |
| `eq`     | Igual a       |
| `sw`     | Empieza con   |
| `ew`     | Termina con   |

**Ejemplos:**

```
GET /api/users?filter=name+co+user
GET /api/users?filter=email+ew+mail.com
GET /api/users?filter=phone+sw+555
GET /api/users?filter=tax_id+eq+AARR990101XXX
```

---

## Autenticación JWT

Todos los endpoints protegidos requieren el header:

```
Authorization: Bearer <JWT_TOKEN>
```

El token se obtiene desde `/api/auth/login` y tiene una validez de 1 hora.

---

## Crear usuario

```
POST /api/users
```

```json
{
  "email": "nuevo@mail.com",
  "name": "Nuevo Usuario",
  "phone": "+52 55 1234 5678",
  "password": "miPassword",
  "tax_id": "XAXX010101AAA",
  "addresses": [
    {
      "name": "casa",
      "street": "Calle 123",
      "country_code": "MX"
    }
  ]
}
```

---

## Validaciones

- **tax_id** — formato RFC mexicano: 4 letras + 6 dígitos + 3 caracteres alfanuméricos (ej. `AARR990101XXX`). Debe ser único.
- **phone** — 10 dígitos, puede incluir código de país. Debe pasar la validación "AndresFormat".
- **email** — formato de email válido.
- **password** — se cifra con AES-256 antes de almacenarse y se excluye de las respuestas.

---

## Zona horaria

El campo `created_at` se genera automáticamente con la fecha/hora actual en la zona horaria de **Madagascar** (`Indian/Antananarivo`) con formato `dd-MM-yyyy HH:mm`.

---

## Tests

Ejecutar las pruebas unitarias:

```bash
./mvnw test
```

Se incluyen tests para:

- `AuthServiceTest` — autenticación y generación de tokens.
- `JwtServiceTest` — generación, extracción y validación de JWT.
- `UserServiceTest` — operaciones CRUD de usuarios.

---

## Docker

### Construir la imagen

```bash
docker build -t chakray-code-test .
```

### Docker Compose

```bash
docker-compose up --build
```

Servicios levantados:

- **chakraydb** — PostgreSQL 16 en puerto `5432`
- **app** — API Spring Boot en puerto `8080`

---

## Estructura del proyecto

```
src/main/java/alex/code/chakrayCodeTest/
├── config/          → Configuración de Spring Security
├── controller/      → Controladores REST (Auth, Users)
├── dto/             → Objetos de transferencia de datos
├── exception/       → Manejo global de excepciones
├── model/           → Entidades JPA (User, Address)
├── repository/      → Repositorios Spring Data JPA
├── security/        → Filtro JWT
├── service/         → Lógica de negocio
└── specification/   → Filtrado dinámico con JPA Criteria API
```

---

## Autor

**Alejandro Lujan**
