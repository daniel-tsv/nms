# NMS Project

## Overview

NMS (Note Management System) is a web application designed for managing notes. The application includes both a frontend built with modern web technologies and a backend RESTful API. The backend is responsible for user authentication, role-based authorization, and CRUD operations for notes, while the frontend provides user interface for interacting with these features.

## Technologies

### Backend

- **Core Language:** Java
- **Build Tool:** Gradle
- **Frameworks:**
  - Spring Boot (Spring Web, Spring Security, Spring Data JPA)
- **Database:** PostgreSQL
- **Libraries:**
  - Lombok
  - Hibernate Validation
  - MapStruct
  - Java-JWT
  - JavaFaker
- **Testing:** JUnit, Mockito (Spring Boot Test, Spring Security Test)

### Frontend

- **Core Language:** TypeScript
- **Build Tool:** Vite
- **Core Library:** React
- **State Management:** Redux Toolkit
- **HTTP Client:** Axios

## Setup

To run this project locally:

1. Clone the repository to your machine.
2. Navigate to the project directory.
3. Run `docker compose up` to start the application.

The backend will be accessible at `http://localhost:8081/api` and the frontend at `http://localhost:4173`.

## Backend Endpoints

All endpoints are accessible under the `/api` prefix.

### Authentication and Authorization

| Method | Endpoint    | Description                                     |
| ------ | ----------- | ----------------------------------------------- |
| POST   | `/login`    | Authenticates a user and generates a JWT token. |
| POST   | `/register` | Registers a new user and generates a JWT token. |

### User Management (Authenticated Users Only)

| Method | Endpoint | Description                                    |
| ------ | -------- | ---------------------------------------------- |
| GET    | `/user`  | Fetches the profile of the authenticated user. |
| PATCH  | `/user`  | Updates the profile of the authenticated user. |
| DELETE | `/user`  | Deletes the profile of the authenticated user. |

### Note Management (Authenticated Users Only)

| Method | Endpoint               | Description                                                             |
| ------ | ---------------------- | ----------------------------------------------------------------------- |
| GET    | `/notes/title/{title}` | Fetches a note by its title.                                            |
| GET    | `/notes`               | Fetches all notes with pagination and sorting options.                  |
| GET    | `/notes/search`        | Searches for notes based on a search term, with pagination and sorting. |
| POST   | `/notes`               | Creates a new note with specified title and optionally contents.        |
| PATCH  | `/notes/title/{title}` | Updates a note by its title.                                            |
| DELETE | `/notes/title/{title}` | Deletes a note by its title.                                            |

#### Pagination and Sorting Options

##### `/notes`

Fetches all notes with pagination and sorting options.

- **Pagination Parameters:**

  - `page` (default = 0): Page number, must not be negative.
  - `size` (default = 20): Number of elements on the page, must be greater than 0.

- **Sorting Parameters:**
  - `direction` (default = asc): Sort direction of elements on the page. Possible values: 'asc' (ascending), 'desc' (descending).
  - `sortBy` (default = updatedAt): Specifies the field by which elements will be sorted. Possible values: 'title', 'createdAt', 'updatedAt'.

##### `/notes/search`

Searches for notes based on a search term, with pagination and sorting options.

- **Required Parameters:**

  - `term`: The search term to perform the search.

- **Optional Parameters:**

  - `searchInContents` (default = false): If set to true, searches for the term in the content of the notes rather than in their titles.

- **Pagination Parameters:**

  - `page` (default = 0): Page number, must not be negative.
  - `size` (default = 20): Number of elements on the page, must be greater than 0.

- **Sorting Parameters:**
  - `direction` (default = asc): Sort direction of elements on the page. Possible values: 'asc' (ascending), 'desc' (descending).
  - `sortBy` (default = updatedAt): Specifies the field by which elements will be sorted. Possible values: 'title', 'createdAt', 'updatedAt'.

### Role Management (Admin Users Only)

| Method | Endpoint            | Description               |
| ------ | ------------------- | ------------------------- |
| POST   | `/admin/roles`      | Creates a new role.       |
| GET    | `/admin/roles/{id}` | Fetches a role by its ID. |
| GET    | `/admin/roles`      | Fetches all roles.        |
| PATCH  | `/admin/roles/{id}` | Updates a role by its ID. |
| DELETE | `/admin/roles/{id}` | Deletes a role by its ID. |

### User Administration (Admin Users Only)

| Method | Endpoint                           | Description                            |
| ------ | ---------------------------------- | -------------------------------------- |
| GET    | `/admin/users`                     | Fetches all users.                     |
| GET    | `/admin/users/{id}`                | Fetches a user by their ID.            |
| PATCH  | `/admin/users/{id}`                | Updates user access fields by user ID. |
| DELETE | `/admin/users/{id}`                | Deletes a user by their ID.            |
| POST   | `/admin/users/{id}/roles/{roleId}` | Assigns a role to a user.              |
| DELETE | `/admin/users/{id}/roles/{roleId}` | Removes a role from a user.            |

## License

This project is licensed under the MIT License.
