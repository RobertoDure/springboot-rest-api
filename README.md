# Spring Boot REST API

This project provides a Spring Boot-based REST API, documented using an OpenAPI 3.0 contract.
School API is a simple API that manages students and teachers.
- **Student**: Manages student information.
- **Teacher**: Manages teacher information.
- **Authentication**: Uses JWT for authentication.
- **Database**: Uses MySQL.
- **Testing**: Includes unit tests.
- **Swagger Documentation**: Provides API documentation.

## Project Structure
- **School_OpenAPI_3.0_Contract.yaml**: Defines API endpoints, request/response schemas, and server information.
- **src/main**: Contains the Java source code.
- **pom.xml**: Manages dependencies.

## Running the Project
1. Build and run the application:
    - Maven: `mvn clean install && mvn spring-boot:run`
2. Access the API at [http://localhost:8080/](http://localhost:8080/).

## Docker Compose
1. Run the following command to start the MySQL database:
    - `docker-compose up -d`
    - The database is accessible at `localhost:3306`.
    - The database name is `school`.

## Dependencies
- **Spring Boot Web**: Provides a web server and Spring MVC.
- **Spring Boot Security**: Provides authentication and authorization.
- **Spring Boot Data JPA**: Provides JPA support.
- **MySQL Database**: docker container.
- **Spring Boot Validation**: Provides validation support.

## Security
The API uses JWT for authentication.
- **/login**: Returns a JWT token.
    - **username**: admin
    - **password**: admin
- **/api/v1/student**: Requires a valid JWT token.
- **/api/v1/teacher**: Requires a valid JWT token.

## Endpoints
- **/login**: Authenticates a user and returns a token.
- **/api/v1/student**: Operations for listing, updating, saving, and deleting students.
- **/api/v1/teacher**: Operations for listing, updating, saving, and deleting teachers.

## OpenAPI Specification
The OpenAPI contract file defines request bodies, parameters, and response structures.  
Use this file to generate API clients or documentation.


## Database
The project uses a MySQL database.
- **Database Name**: school
- **Tables**: student, teacher
- **Username**: root
- **Password**: root
- **Port**: 3306
- **Host**: localhost
- **Docker Container**: mysql:5.7

## Testing
The project includes unit tests for the service layer.

## Swagger Documentation

URL path: /v2/api-docs

URL Swagger UI path: /swagger-ui.html
