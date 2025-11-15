# User Profiles Management System

A full-stack application for managing user profiles with Java Spring Boot backend and React frontend.

## Features

- ✅ Complete CRUD operations for user profiles
- ✅ Database model with JPA/Hibernate
- ✅ RESTful API with validation
- ✅ React UI with form validation
- ✅ Comprehensive test suite
- ✅ Email uniqueness validation
- ✅ Phone number validation
- ✅ URL validation for website and avatar

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory, can be switched to PostgreSQL)
- **Gradle** (build tool)
- **Lombok** (reducing boilerplate)
- **JUnit 5** (testing)

### Frontend
- **React 18**
- **Vite** (build tool)
- **Axios** (HTTP client)

## Project Structure

```
demo-cursor/
├── src/
│   ├── main/
│   │   ├── java/com/example/userprofiles/
│   │   │   ├── UserProfilesApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── UserProfileController.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── service/
│   │   │   │   └── UserProfileService.java
│   │   │   ├── repository/
│   │   │   │   └── UserProfileRepository.java
│   │   │   ├── model/
│   │   │   │   └── UserProfile.java
│   │   │   └── dto/
│   │   │       ├── UserProfileCreateDTO.java
│   │   │       ├── UserProfileUpdateDTO.java
│   │   │       └── UserProfileResponseDTO.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/userprofiles/
│           ├── UserProfileControllerTest.java
│           └── UserProfileServiceTest.java
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── ProfileForm.jsx
│   │   │   └── ProfileList.jsx
│   │   ├── App.jsx
│   │   └── main.jsx
│   └── package.json
├── build.gradle
└── settings.gradle
```

## Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **Gradle 7.6+** (or use Gradle Wrapper)

## Setup Instructions

### Backend Setup

1. **Navigate to the project root:**
   ```bash
   cd /Users/paramita.santra/impks/demo-cursor
   ```

2. **Build the project:**
   ```bash
   ./gradlew build
   ```

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```
   
   Or if you have Gradle installed globally:
   ```bash
   gradle bootRun
   ```

4. **The API will be available at:**
   - API Base URL: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
   - API Docs: `http://localhost:8080/api/profiles`

### Frontend Setup

1. **Navigate to the frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm run dev
   ```

4. **The frontend will be available at:**
   - `http://localhost:3000`

## API Endpoints

### Get All Profiles
```
GET /api/profiles?skip=0&limit=100
```

### Get Profile by ID
```
GET /api/profiles/{id}
```

### Create Profile
```
POST /api/profiles
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "bio": "Software developer",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phone": "1234567890",
  "location": "New York",
  "website": "https://johndoe.com"
}
```

### Update Profile
```
PUT /api/profiles/{id}
Content-Type: application/json

{
  "name": "John Updated",
  "bio": "Updated bio"
}
```

### Delete Profile
```
DELETE /api/profiles/{id}
```

## Validation Rules

### Name
- Required
- 2-100 characters
- Only letters, spaces, hyphens, apostrophes, and periods

### Email
- Required
- Valid email format
- Unique (no duplicates)
- Max 255 characters

### Bio
- Optional
- Max 1000 characters

### Phone
- Optional
- 10-15 digits (after removing formatting)
- Can contain spaces, hyphens, parentheses, and plus signs

### Website & Avatar URL
- Optional
- Must be valid URL format
- Must start with http:// or https://

### Location
- Optional
- Max 100 characters

## Running Tests

### Backend Tests
```bash
./gradlew test
```

### View Test Reports
```bash
open build/reports/tests/test/index.html
```

## Database

The application uses H2 in-memory database by default. To switch to PostgreSQL:

1. Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/userprofiles
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   ```

2. Add PostgreSQL dependency to `build.gradle`:
   ```gradle
   runtimeOnly 'org.postgresql:postgresql'
   ```

## Development

### Hot Reload
- Backend: Use Spring Boot DevTools (add to dependencies)
- Frontend: Vite provides hot module replacement automatically

### Accessing H2 Console
1. Go to `http://localhost:8080/h2-console`
2. JDBC URL: `jdbc:h2:mem:userprofilesdb`
3. Username: `sa`
4. Password: (leave empty)

## Testing Git Workflow

This is a test change to verify the git workflow is working correctly.
