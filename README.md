# ms-autenticacion — Microservicio de Autenticación

Microservicio correspondiente al **caso NovaBank** (Banca digital / fintech) de la **Evaluación Parcial N°2**.

| | |
|---|---|
| **Asignatura** | JVY0101 — Java: Diseño y Construcción de Soluciones Nativas en Nube |
| **Stack** | Spring Boot 3.2.5 · Java 17 · Maven Wrapper · Spring Data JPA · H2 Database |
| **Arquitectura** | Capas (Controller, Service, Repository, Model) |
| **Puerto** | 8081 |

## Responsabilidad (SRP)

Administra el registro y la autenticación de los usuarios de NovaBank, validando el RUT chileno (dígito verificador), la unicidad de RUT y email, y los roles permitidos (CLIENTE, OPERADOR, AUDITOR). Emite un token de sesión simulado en el login y controla el estado activo del usuario. Su base de datos es una **H2 en memoria** (`jdbc:h2:mem:novabank_auth_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`), cumpliendo aislamiento de datos por dominio.

## Consola y Persistencia

Al ejecutar el servicio en el puerto `8081`, se puede acceder a la consola de persistencia H2:

- **H2 Console**: `http://localhost:8081/h2-console`
- **JDBC URL**: `jdbc:h2:mem:novabank_auth_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- **Usuario**: `sa`
- **Contraseña**: *(en blanco)*

## Endpoints Principales (`/api/auth`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/registro` | Registra un usuario validando RUT (dígito verificador), email, rol (`CLIENTE`/`OPERADOR`/`AUDITOR`) y unicidad de RUT/email; responde 201 o 400 si los datos son inválidos. |
| POST | `/api/auth/login` | Autentica por RUT y password; devuelve un token simulado o responde 401 si las credenciales son inválidas o la cuenta está inactiva. |
| GET | `/api/auth/usuarios` | Lista todos los usuarios registrados. |
| GET | `/api/auth/usuarios/{id}` | Obtiene un usuario por id; responde 404 si no existe. |
| DELETE | `/api/auth/usuarios/{id}` | Elimina un usuario por id; responde 204 si tuvo éxito o 404 si no existe. |

## Instrucciones de Clonación, Compilación y Ejecución Local

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd ms-autenticacion
   ```

2. **Compilar y empaquetar con Maven Wrapper:**
   ```bash
   .\mvnw.cmd clean package -DskipTests
   ```

3. **Ejecutar el ejecutable .jar generado:**
   ```bash
   java -jar target/ms-autenticacion-1.0.0.jar
   ```

## Cómo ejecutar las pruebas

```bash
.\mvnw.cmd test
```
