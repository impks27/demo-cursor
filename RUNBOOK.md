# User Profiles Application - Runbook

## Table of Contents
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Initial Setup](#initial-setup)
4. [Launch Procedures](#launch-procedures)
5. [Testing Procedures](#testing-procedures)
6. [API Testing](#api-testing)
7. [Frontend Testing](#frontend-testing)
8. [Troubleshooting](#troubleshooting)
9. [Maintenance](#maintenance)
10. [Shutdown Procedures](#shutdown-procedures)

---

## Overview

This runbook provides step-by-step instructions for launching, testing, and maintaining the User Profiles Management System. The application consists of:

- **Backend**: Java Spring Boot REST API (Port 8080)
- **Frontend**: React application with Vite (Port 3000)
- **Database**: H2 in-memory database

---

## Prerequisites

### Required Software
- **Java 17** or higher
  ```bash
  java -version
  # Should show version 17 or higher
  ```

- **Node.js 18** or higher
  ```bash
  node -version
  # Should show v18.x.x or higher
  ```

- **npm** (comes with Node.js)
  ```bash
  npm -version
  ```

- **Gradle** (optional - wrapper is included)
  ```bash
  ./gradlew --version
  ```

### System Requirements
- Minimum 2GB RAM
- 500MB free disk space
- Ports 8080 and 3000 available

### Verify Prerequisites
```bash
# Check Java
java -version

# Check Node.js
node -version
npm -version

# Check ports availability (macOS/Linux)
lsof -i :8080
lsof -i :3000

# If ports are in use, you'll need to stop those processes or change ports
```

---

## Initial Setup

### Step 1: Clone/Navigate to Project
```bash
cd /Users/paramita.santra/impks/demo-cursor
```

### Step 2: Verify Project Structure
```bash
# Check key files exist
ls -la build.gradle
ls -la settings.gradle
ls -la frontend/package.json
ls -la src/main/java/com/example/userprofiles/UserProfilesApplication.java
```

### Step 3: Build Backend (First Time)
```bash
# Clean and build the project
./gradlew clean build

# Expected output: BUILD SUCCESSFUL
```

**Troubleshooting Build Issues:**
- If build fails, check Java version: `java -version`
- Ensure you're using Java 17+
- Try: `./gradlew clean build --refresh-dependencies`

### Step 4: Install Frontend Dependencies (First Time)
```bash
cd frontend
npm install

# Expected: "added X packages"
```

**Troubleshooting npm Issues:**
- If npm install fails, try: `npm cache clean --force`
- Delete `node_modules` and `package-lock.json`, then retry
- Check network connectivity

---

## Launch Procedures

### Launch Backend Server

#### Option 1: Using Gradle Wrapper (Recommended)
```bash
# From project root
./gradlew bootRun
```

#### Option 2: Using Gradle (if installed globally)
```bash
gradle bootRun
```

#### Option 3: Using JAR file
```bash
# Build JAR first
./gradlew bootJar

# Run JAR
java -jar build/libs/user-profiles-1.0.0.jar
```

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

... (Spring Boot startup logs)
Started UserProfilesApplication in X.XXX seconds
```

**Verification:**
```bash
# Wait 10-15 seconds for startup, then test
curl http://localhost:8080/api/profiles

# Should return: [] (empty array) or list of profiles
```

### Launch Frontend Server

**In a NEW terminal window:**
```bash
cd /Users/paramita.santra/impks/demo-cursor/frontend
npm run dev
```

**Expected Output:**
```
  VITE v5.x.x  ready in XXX ms

  ➜  Local:   http://localhost:3000/
  ➜  Network: use --host to expose
```

**Verification:**
- Open browser: http://localhost:3000
- Should see "User Profiles" header and "Create New Profile" button

### Launch Both Services (Background)

**Backend (Background):**
```bash
cd /Users/paramita.santra/impks/demo-cursor
./gradlew bootRun > backend.log 2>&1 &
echo $! > backend.pid
```

**Frontend (Background):**
```bash
cd /Users/paramita.santra/impks/demo-cursor/frontend
npm run dev > ../frontend.log 2>&1 &
echo $! > ../frontend.pid
```

**Check Status:**
```bash
# Check if processes are running
ps aux | grep -E "bootRun|vite"

# Check logs
tail -f backend.log
tail -f frontend.log
```

---

## Testing Procedures

### Pre-Test Checklist
- [ ] Backend is running on port 8080
- [ ] Frontend is running on port 3000
- [ ] No errors in console/logs
- [ ] Database is accessible (H2 console)

### Health Check Tests

#### 1. Backend Health Check
```bash
# Test root endpoint
curl http://localhost:8080/

# Test API endpoint
curl http://localhost:8080/api/profiles

# Expected: [] or JSON array
```

#### 2. Frontend Health Check
```bash
# Test frontend accessibility
curl http://localhost:3000

# Expected: HTML content
```

#### 3. Database Health Check
- Open browser: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:userprofilesdb`
- Username: `sa`
- Password: (leave empty)
- Click "Connect"
- Run query: `SELECT * FROM USER_PROFILES;`

---

## API Testing

### Test 1: Create Profile (POST)

**Request:**
```bash
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "bio": "Software developer",
    "phone": "1234567890",
    "location": "New York",
    "website": "https://johndoe.com"
  }'
```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "bio": "Software developer",
  "avatarUrl": null,
  "phone": "1234567890",
  "location": "New York",
  "website": "https://johndoe.com",
  "createdAt": "2025-11-16T...",
  "updatedAt": "2025-11-16T..."
}
```

**Success Criteria:**
- HTTP Status: 201
- Response contains all fields
- ID is generated
- Timestamps are present

### Test 2: Get All Profiles (GET)

**Request:**
```bash
curl http://localhost:8080/api/profiles
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    ...
  }
]
```

**Success Criteria:**
- HTTP Status: 200
- Returns array (even if empty)
- All profiles are included

### Test 3: Get Profile by ID (GET)

**Request:**
```bash
curl http://localhost:8080/api/profiles/1
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  ...
}
```

**Success Criteria:**
- HTTP Status: 200
- Returns single profile
- ID matches request

### Test 4: Update Profile (PUT)

**Request:**
```bash
curl -X PUT http://localhost:8080/api/profiles/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "bio": "Senior Software Developer"
  }'
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Updated",
  "bio": "Senior Software Developer",
  ...
  "updatedAt": "2025-11-16T..." // Should be updated
}
```

**Success Criteria:**
- HTTP Status: 200
- Fields are updated
- `updatedAt` timestamp changes
- Other fields remain unchanged

### Test 5: Delete Profile (DELETE)

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/profiles/1
```

**Expected Response (204 No Content):**
- Empty response body
- HTTP Status: 204

**Verification:**
```bash
curl http://localhost:8080/api/profiles/1
# Should return 404 Not Found
```

### Test 6: Validation Tests

#### Test 6a: Duplicate Email
```bash
# Create first profile
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"test@example.com"}'

# Try to create duplicate
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane","email":"test@example.com"}'
```

**Expected:** HTTP 400 with error message

#### Test 6b: Invalid Email Format
```bash
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"invalid-email"}'
```

**Expected:** HTTP 400 with validation errors

#### Test 6c: Short Name
```bash
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"name":"J","email":"test@example.com"}'
```

**Expected:** HTTP 400 with validation errors

#### Test 6d: Invalid Phone Number
```bash
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"test@example.com","phone":"123"}'
```

**Expected:** HTTP 400 with validation errors

### Test 7: Error Handling

#### Test 7a: Get Non-Existent Profile
```bash
curl http://localhost:8080/api/profiles/999
```

**Expected:** HTTP 404 Not Found

#### Test 7b: Update Non-Existent Profile
```bash
curl -X PUT http://localhost:8080/api/profiles/999 \
  -H "Content-Type: application/json" \
  -d '{"name":"Test"}'
```

**Expected:** HTTP 404 Not Found

#### Test 7c: Delete Non-Existent Profile
```bash
curl -X DELETE http://localhost:8080/api/profiles/999
```

**Expected:** HTTP 404 Not Found

### Automated Test Script

Create a test script `test-api.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api/profiles"

echo "=== Testing User Profiles API ==="

# Test 1: Create Profile
echo -e "\n1. Creating profile..."
RESPONSE=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","bio":"Test bio"}')
echo $RESPONSE | python3 -m json.tool

# Extract ID
ID=$(echo $RESPONSE | python3 -c "import sys, json; print(json.load(sys.stdin)['id'])")

# Test 2: Get All
echo -e "\n2. Getting all profiles..."
curl -s $BASE_URL | python3 -m json.tool

# Test 3: Get by ID
echo -e "\n3. Getting profile by ID: $ID"
curl -s $BASE_URL/$ID | python3 -m json.tool

# Test 4: Update
echo -e "\n4. Updating profile..."
curl -s -X PUT $BASE_URL/$ID \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated User"}' | python3 -m json.tool

# Test 5: Delete
echo -e "\n5. Deleting profile..."
curl -s -X DELETE $BASE_URL/$ID -w "\nHTTP Status: %{http_code}\n"

echo -e "\n=== Tests Complete ==="
```

**Run the script:**
```bash
chmod +x test-api.sh
./test-api.sh
```

---

## Frontend Testing

### Manual UI Tests

#### Test 1: Create Profile via UI
1. Open http://localhost:3000
2. Click "Create New Profile"
3. Fill in the form:
   - Name: "Jane Smith"
   - Email: "jane@example.com"
   - Bio: "Frontend developer"
   - Phone: "9876543210"
   - Location: "San Francisco"
   - Website: "https://janesmith.com"
4. Click "Create"
5. **Verify:** Profile appears in the list

#### Test 2: Edit Profile via UI
1. Click "Edit" on any profile
2. Modify fields (e.g., change bio)
3. Click "Update"
4. **Verify:** Changes are reflected in the list

#### Test 3: Delete Profile via UI
1. Click "Delete" on a profile
2. Confirm deletion
3. **Verify:** Profile is removed from the list

#### Test 4: Form Validation
1. Click "Create New Profile"
2. Try submitting with:
   - Empty name → Should show error
   - Invalid email → Should show error
   - Short name (< 2 chars) → Should show error
   - Phone too short → Should show error
3. **Verify:** Error messages appear

#### Test 5: Duplicate Email Handling
1. Create a profile with email "test@example.com"
2. Try to create another with same email
3. **Verify:** Error message appears

### Browser Console Tests

Open browser DevTools (F12) and check:
- No console errors
- Network requests are successful (200/201 status)
- Failed requests show appropriate error handling

---

## Troubleshooting

### Issue 1: Backend Won't Start

**Symptoms:**
- Port 8080 already in use
- Build failures
- Application crashes on startup

**Solutions:**

```bash
# Check if port is in use
lsof -i :8080

# Kill process using port (replace PID)
kill -9 <PID>

# Or change port in application.properties
# server.port=8081

# Check Java version
java -version  # Should be 17+

# Clean and rebuild
./gradlew clean build

# Check for compilation errors
./gradlew compileJava
```

### Issue 2: Frontend Won't Start

**Symptoms:**
- Port 3000 already in use
- npm install fails
- Module not found errors

**Solutions:**

```bash
# Check if port is in use
lsof -i :3000

# Kill process using port
kill -9 <PID>

# Or change port in vite.config.js
# server: { port: 3001 }

# Clear npm cache and reinstall
cd frontend
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Issue 3: API Returns 404

**Symptoms:**
- Frontend can't connect to backend
- CORS errors in browser console

**Solutions:**

```bash
# Verify backend is running
curl http://localhost:8080/api/profiles

# Check CORS configuration in UserProfileController.java
# Should have: @CrossOrigin(origins = {"http://localhost:3000"})

# Check frontend API URL in App.jsx
# Should be: http://localhost:8080/api/profiles
```

### Issue 4: Database Issues

**Symptoms:**
- Data not persisting
- Database connection errors

**Solutions:**

```bash
# H2 is in-memory, data is lost on restart
# This is expected behavior

# To persist data, switch to file-based H2:
# In application.properties:
# spring.datasource.url=jdbc:h2:file:./data/userprofilesdb

# Or switch to PostgreSQL (see README)
```

### Issue 5: Validation Not Working

**Symptoms:**
- Invalid data accepted
- No error messages

**Solutions:**

```bash
# Check DTO validation annotations
# Verify @Valid annotation in controller
# Check GlobalExceptionHandler is present

# Test validation manually:
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"name":"J","email":"invalid"}'
# Should return 400 with errors
```

### Issue 6: Tests Failing

**Symptoms:**
- JUnit tests fail
- Test database issues

**Solutions:**

```bash
# Run tests with verbose output
./gradlew test --info

# Run specific test
./gradlew test --tests UserProfileControllerTest

# Check test database configuration
# application-test.properties should exist

# Clean and rebuild
./gradlew clean test
```

### Common Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| `Port 8080 already in use` | Another process using port | Kill process or change port |
| `Cannot find module` | Missing npm dependencies | Run `npm install` |
| `ClassNotFoundException` | Missing Java dependencies | Run `./gradlew build` |
| `CORS error` | Frontend can't access backend | Check CORS configuration |
| `404 Not Found` | Wrong URL or backend not running | Verify backend is running |
| `500 Internal Server Error` | Server-side error | Check backend logs |

---

## Maintenance

### Daily Tasks
- Monitor application logs
- Check database size (if using file-based H2)
- Verify services are running

### Weekly Tasks
- Review error logs
- Check for dependency updates
- Run full test suite

### Log Locations
- Backend logs: Console output or `backend.log` (if running in background)
- Frontend logs: Browser console or `frontend.log` (if running in background)

### Backup Procedures
If using file-based database:
```bash
# Backup H2 database file
cp data/userprofilesdb.mv.db backups/userprofilesdb-$(date +%Y%m%d).mv.db
```

### Update Dependencies

**Backend:**
```bash
./gradlew dependencyUpdates
# Review and update build.gradle
./gradlew clean build
```

**Frontend:**
```bash
cd frontend
npm outdated
npm update
npm install
```

---

## Shutdown Procedures

### Graceful Shutdown

**Backend:**
```bash
# If running in foreground: Ctrl+C

# If running in background:
kill $(cat backend.pid)
```

**Frontend:**
```bash
# If running in foreground: Ctrl+C

# If running in background:
kill $(cat frontend.pid)
```

### Force Shutdown

```bash
# Find and kill processes
pkill -f "bootRun"
pkill -f "vite"

# Or by port
lsof -ti:8080 | xargs kill -9
lsof -ti:3000 | xargs kill -9
```

### Cleanup

```bash
# Remove build artifacts
./gradlew clean

# Remove frontend build
cd frontend
rm -rf dist node_modules/.vite

# Remove logs
rm -f backend.log frontend.log *.pid
```

---

## Quick Reference

### Ports
- Backend: 8080
- Frontend: 3000
- H2 Console: 8080/h2-console

### Key URLs
- Frontend: http://localhost:3000
- API Base: http://localhost:8080/api/profiles
- H2 Console: http://localhost:8080/h2-console

### Key Commands
```bash
# Start backend
./gradlew bootRun

# Start frontend
cd frontend && npm run dev

# Run tests
./gradlew test

# Build
./gradlew build

# Clean
./gradlew clean
```

### Database Credentials
- JDBC URL: `jdbc:h2:mem:userprofilesdb`
- Username: `sa`
- Password: (empty)

---

## Support Contacts

For issues or questions:
1. Check this runbook
2. Review application logs
3. Check GitHub issues (if applicable)
4. Contact development team

---

**Last Updated:** November 16, 2025
**Version:** 1.0.0

