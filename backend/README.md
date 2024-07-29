### Overview
This is a simple RESTful API for managing notes. It's built with Java, Spring Boot, Spring Security, and PostgreSQL, among others. The API provides functions such as registration and login, JWT token generation, verification, handling, role-based authorization, and notes/users/roles management.
### Technologies
- Core Language: Java
- Build Tool: Gradle
- Framework: Spring Boot, Spring Web, Spring Security, Spring Data JPA
- Testing: JUnit, Mockito (Spring Boot Test, Spring Security Test)
- Database: PostgreSQL
- Libraries: Lombok, Hibernate Validation, MapStruct, Java-JWT, JavaFaker
### Setup
To run this project locally:
- Clone the repository to your machine.
- Open the project in your IDE.
- Configure PostgreSQL database in the application.yml file.
- Run the app.
### Endpoints
1. AuthController:
    - POST /auth/login: Authenticates a user and generates a JWT token.
    - POST /auth/register: Registers and authenticates a new user and generates a JWT token.
2. UserController (only accesible to authenticated users):
    - GET /user: Fetches the profile of the authenticated user.
    - PATCH /user: Updates the profile of the authenticated user.
    - DELETE /user: Deletes the profile of the authenticated user.   
3. NoteController (only accesible to authenticated users):
    - GET /notes/title/{title}: Fetches a note by its title.
    - GET /notes: Fetches all notes with pagination and sorting options.
        - Pagination & sorting options are available as request parameteres:
            - page (default = 0): page number, must not be negative.
            - size (default = 20): number of elements on page, must be greater than 0.
            - direction (default = asc): sort direction of elements on page. Possible values: 'asc' - ascending, 'desc' - descending.
            - sortBy (default = updatedAt): specifies Note field by which elements will be sorted. Possible values: 'title', 'createdAt', 'updatedAt'.
    - GET /notes/search: Searches for notes based on a search term, with pagination and sorting options.
        - 'term': required parameter to perform the search.
        - 'searchInContents' (default = false): parameter can be specified to search for a 'term' in the content of the notes rather than in their titles.  
        - Pagination & sorting options are also available as request parameteres:
            - page (default = 0): page number, must not be negative.
            - size (default = 20): number of elements on page, must be greater than 0.
            - direction (default = asc): sort direction of elements on page. Possible values: 'asc' - ascending, 'desc' - descending.
            - sortBy (default = updatedAt): specifies Note field by which elements will be sorted. Possible values: 'title', 'createdAt', 'updatedAt'.
    - POST /notes: Creates a new note with specified 'title', and optionally 'contents'. Returns location of created note.
    - PATCH /notes/title/{title}: Updates a note by its title. Updates 'title' and optionally 'contents'.
    - DELETE /notes/title/{title}: Deletes a note by its title.
4. RoleController (only accesible to admin users):
    - POST /admin/roles: Creates a new role.
    - GET /admin/roles/{id}: Fetches a role by its ID.
    - GET /admin/roles: Fetches all roles.
    - PATCH /admin/roles/{id}: Updates a role by its ID.
    - DELETE /admin/roles/{id}: Deletes a role by its ID.
5. AdminUserController (only accesible to admin users):
    - GET /admin/users: Fetches all users.
    - GET /admin/users/{id}: Fetches a user by their ID.
    - PATCH /admin/users/{id}: Updates user access boolean fields by user ID.
        - Modifiable fields: isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled.
        - Null values are ignored.
    - DELETE /admin/users/{id}: Deletes a user by their ID.
    - POST /admin/users/{id}/roles/{roleId}: Assigns a role with ID {roleId}, to user with ID {id}
    - DELETE /admin/users/{id}/roles/{roleId}: Removes a role with ID {roleId}, from user with ID {id}
### License
This project is licensed under the WTFPL.
