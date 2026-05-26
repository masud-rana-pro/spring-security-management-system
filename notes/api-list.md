# API List - Secure Auth Lab

List of all available API endpoints in the backend application.

## Base URL
```
http://localhost:8080
```

---

## Public Endpoints (No Authentication Required)

### GET /api/public/hello
- **Description**: Simple health check to verify server is running
- **Auth Required**: No
- **Response**: String
  ```text
  Hello Secure Auth Lab
  ```

---

## Authentication Endpoints (No Authentication Required)

### POST /api/auth/register
- **Description**: Register a new user account
- **Auth Required**: No
- **Request Body** (JSON):
  ```json
  {
    "name": "string (required)",
    "email": "string (required, must be valid email)",
    "password": "string (required, minimum 6 characters)"
  }
  ```
- **Success Response** (201):
  ```json
  {
    "message": "Registration successfull",
    "email": "user@example.com",
    "role": "USER",
    "token": null
  }
  ```
- **Error Response** (400) - Duplicate Email:
  ```json
  {
    "timestamp": "2026-05-27T00:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Email already exists",
    "path": "/api/auth/register",
    "validationErrors": null
  }
  ```
- **Error Response** (400) - Validation Failed:
  ```json
  {
    "timestamp": "2026-05-27T00:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "path": "/api/auth/register",
    "validationErrors": {
      "name": "Name is required",
      "email": "Email is invalid",
      "password": "Password must be at least 6 characters long"
    }
  }
  ```

---

### POST /api/auth/login
- **Description**: Authenticate user and receive JWT token
- **Auth Required**: No
- **Request Body** (JSON):
  ```json
  {
    "email": "string (required, must be valid email)",
    "password": "string (required)"
  }
  ```
- **Success Response** (200):
  ```json
  {
    "message": "Login successful",
    "email": "user@example.com",
    "role": "USER",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWI... (JWT token)"
  }
  ```
- **Error Response** (400) - Invalid Credentials:
  ```json
  {
    "timestamp": "2026-05-27T00:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid email or password",
    "path": "/api/auth/login",
    "validationErrors": null
  }
  ```

---

## Protected Endpoints (Authentication Required)

### GET /api/user/profile
- **Description**: Get profile of the currently authenticated user
- **Auth Required**: Yes (any authenticated user - USER or ADMIN)
- **Headers Required**: `Authorization: Bearer <token>`
- **Success Response** (200):
  ```text
  User Profile - Email: user@example.com, Name: John Doe, Role: USER
  ```
- **Error Response** (401) - No Token:
  ```json
  {
    "timestamp": "2026-05-27T00:00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied",
    "path": "/api/user/profile",
    "validationErrors": null
  }
  ```

---

### GET /api/admin/dashboard
- **Description**: Admin dashboard showing system statistics
- **Auth Required**: Yes (ADMIN role only)
- **Headers Required**: `Authorization: Bearer <token>`
- **Success Response** (200):
  ```text
  Admin Dashboard - Total registered users: 5
  ```
- **Error Response** (403) - USER trying to access:
  ```json
  {
    "timestamp": "2026-05-27T00:00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied",
    "path": "/api/admin/dashboard",
    "validationErrors": null
  }
  ```

---

## How to Use JWT Token in Postman

1. Call `POST /api/auth/login` with valid credentials
2. Copy the `token` value from the response
3. In Postman, go to **Headers** tab
4. Add header:
   ```
   Key: Authorization
   Value: Bearer <your-token-here>
   ```
5. Now you can access authenticated endpoints

---

## Role-Based Access Summary

| Endpoint | Role Required | Who Can Access |
|----------|--------------|----------------|
| `/api/public/**` | None | Everyone |
| `/api/auth/**` | None | Everyone |
| `/api/user/profile` | ROLE_USER or ROLE_ADMIN | Normal users + Admins |
| `/api/admin/dashboard` | ROLE_ADMIN only | Only Admins |

---

## Planned Endpoints (Not Yet Implemented)

### POST /api/auth/oauth2/google
- **Description**: Login with Google OAuth2
- **Auth Required**: No
- **Status**: Not implemented yet

### POST /api/auth/oauth2/github
- **Description**: Login with GitHub OAuth2
- **Auth Required**: No
- **Status**: Not implemented yet