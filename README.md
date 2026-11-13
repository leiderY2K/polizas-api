# API de Gestión de Pólizas

API REST desarrollada con Spring Boot para la consulta y gestión de pólizas de arrendamiento y sus riesgos asociados.

El proyecto implementa un caso técnico simplificado, con persistencia en memoria, validaciones de negocio y seguridad básica mediante API Key.

## Tecnologías utilizadas

- Java 17
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- H2 Database
- Jakarta Validation
- Maven
- Lombok

## Arquitectura del proyecto

Se utiliza una arquitectura por capas:

```text
src/main/java/
├── controller/     # Endpoints REST
├── service/        # Lógica y reglas de negocio
├── repository/     # Acceso a datos con JPA
├── entity/         # Entidades y enumeraciones
├── dto/            # Solicitudes y respuestas
├── exception/      # Manejo de los errores
└── config/         # Seguridad y datos iniciales
```

La capa `controller` recibe las solicitudes HTTP, `service` ejecuta las reglas de negocio y `repository` gestiona la persistencia.

## Requisitos

- JDK 17, 21 o compatible con Spring Boot 4.1.2.
- Maven o Maven Wrapper incluido en el proyecto.
- Puerto 8080 disponible.

## Instalación y ejecución

Clonar el repositorio:

```bash
git clone https://github.com/leiderY2K/polizas-api.git
cd polizas-api
```

En Windows, ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

## Base de datos

Se utiliza H2 en memoria para facilitar la ejecución sin dependencias externas.

La información se reinicia al detener la aplicación.

El proyecto incluye un cargador de datos iniciales (`DataLoader`) para probar los endpoints sin implementar un proceso adicional de creación de pólizas.

Datos de ejemplo:

| Tipo | Estado | Canon | Prima |
|---|---|---|---|
| INDIVIDUAL | ACTIVA | 1.000.000 | 12.000.000 |
| COLECTIVA | ACTIVA | 1.500.000 | 18.000.000 |

## Seguridad

Todas las solicitudes requieren el header:

```http
x-api-key: 123456
```

Si la clave no se proporciona o es incorrecta, la API responde `401 Unauthorized`.

La clave de prueba puede configurarse mediante la variable de entorno `API_KEY`.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/polizas` | Listar pólizas, con filtros opcionales |
| GET | `/polizas/{id}/riesgos` | Consultar riesgos de una póliza |
| POST | `/polizas/{id}/renovar` | Renovar aplicando IPC |
| POST | `/polizas/{id}/cancelar` | Cancelar póliza y sus riesgos |
| POST | `/polizas/{id}/riesgos` | Agregar riesgo a póliza colectiva |
| POST | `/riesgos/{id}/cancelar` | Cancelar un riesgo |
| POST | `/core-mock/evento` | Registrar un intento de envío al CORE |

### Filtrar pólizas

```http
GET /polizas?tipo=COLECTIVA&estado=ACTIVA
x-api-key: 123456
```

### Renovar póliza

```http
POST /polizas/1/renovar
Content-Type: application/json
x-api-key: 123456
```

```json
{
  "ipc": 5.0
}
```

El IPC se interpreta como porcentaje. Un valor de `5.0` incrementa el canon y la prima un 5 %.

### Agregar riesgo

```http
POST /polizas/2/riesgos
Content-Type: application/json
x-api-key: 123456
```

```json
{
  "descripcion": "Apartamento 202"
}
```

### Cancelar póliza

```http
POST /polizas/1/cancelar
x-api-key: 123456
```

No requiere cuerpo JSON.

### Cancelar riesgo

```http
POST /riesgos/1/cancelar
x-api-key: 123456
```

No requiere cuerpo JSON.

### CORE Mock

```http
POST /core-mock/evento
Content-Type: application/json
x-api-key: 123456
```

```json
{
  "evento": "ACTUALIZACION",
  "polizaId": 555
}
```

El endpoint registra el intento de envío al CORE mediante los logs de la aplicación. Este no establece una conexión con un CORE real.

## Reglas de negocio

- Una póliza individual admite únicamente un riesgo.
- No se permite renovar una póliza cancelada.
- Renovar una póliza incrementa canon y prima según el IPC y cambia el estado a `RENOVADA`.
- Cancelar una póliza también cancela todos sus riesgos.
- Solo las pólizas colectivas permiten agregar riesgos.
- La cancelación de riesgos es lógica; no se eliminan físicamente los registros.

## Manejo de errores

La API contempla respuestas HTTP como:

- `200 OK`: operación exitosa.
- `400 Bad Request`: solicitud o parámetros inválidos.
- `401 Unauthorized`: API Key ausente o incorrecta.
- `409 Conflict`: operación no permitida por las reglas de negocio.

## Consideraciones técnicas

- Se utiliza `BigDecimal` para cálculos monetarios.
- Las operaciones de actualización utilizan `@Transactional`.
- Los DTO separan el modelo de persistencia del contrato HTTP.
- La base H2 permite ejecutar y probar el proyecto de manera local.
- La integración CORE es simulada y no realiza operaciones sobre sistemas externos.