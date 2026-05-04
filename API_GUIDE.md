# Journal App API Guide

## Base URL
All endpoints are prefixed with: `http://localhost:8080/journal`

## Public Endpoints (No Authentication Required)

### 1. Create User
**Endpoint:** `POST /public/create-user`

**Request Body:**
```json
{
  "username": "your_username",
  "password": "your_password"
}
```

**Success Response (201 CREATED):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "username": "your_username",
  "password": "$2a$10$...", // BCrypt encrypted password
  "roles": ["USER"],
  "journalEntryListOfUsers": []
}
```

**Error Responses:**
- **409 CONFLICT**: Username already exists
  ```
  Error: Username 'your_username' already exists. Please choose a different username.
  ```
- **400 BAD REQUEST**: Invalid request or MongoDB error
  ```
  Error: Failed to create user. <error message>
  ```

**Example:**
```bash
curl -X POST http://localhost:8080/journal/public/create-user \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser123","password":"securepassword"}'
```

### 2. Health Check
**Endpoint:** `GET /public/health-check`

**Response:**
```
Application is running successfully!
```

**Example:**
```bash
curl http://localhost:8080/journal/public/health-check
```

## Log Files

Log files are stored at: `/home/abdullah-jutt/Documents/SpringBoot/journalApp/journalApp.log`

**Features:**
- Automatic rolling based on size (10MB) and date
- Debug-level logging showing all application activity
- Both console and file output enabled

**View Recent Logs:**
```bash
tail -100 /home/abdullah-jutt/Documents/SpringBoot/journalApp/journalApp.log
```

## Common Issues & Solutions

### Issue 1: User creation returns 404
**Cause:** Missing `/journal` context path in URL

**Solution:** Use the full path: `http://localhost:8080/journal/public/create-user`

### Issue 2: Duplicate username error
**Cause:** Trying to create a user with a username that already exists

**Solution:** Choose a different username

### Issue 3: No log files being created
**Status:** ✅ RESOLVED - Log files are actively being created and written to

## Configuration Files

- **Main Config:** `src/main/resources/application.yaml`
- **Dev Config:** `src/main/resources/application-dev.yaml`
- **Logging Config:** `src/main/resources/logback.xml`
- **MongoDB Connection:** Configured in `application-dev.yaml`
  - URI: MongoDB Atlas Cluster (shard replicated)
  - Database: `journaldb`
  - Auto index creation: enabled

## Recent Updates

1. **Improved Error Handling** - Better error messages when user creation fails
2. **Health Check** - Enhanced response message
3. **Logging** - Verified log file creation and output

