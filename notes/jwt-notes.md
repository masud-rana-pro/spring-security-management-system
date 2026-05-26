# JWT (JSON Web Token) Notes - Secure Auth Lab

This document explains how JWT works in this project and the overall authentication flow.

---

## What is JWT?

JWT (JSON Web Token) is a compact, URL-safe token format used to transmit authentication data between client and server. The token is digitally signed so it can be verified and trusted.

### JWT Structure

A JWT token has three parts separated by dots (`.`):

```
header.payload.signature
```

Example:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIifQ.abc123...
```

### Part 1: Header (Base64 encoded)
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
- `alg`: Algorithm used for signing (HMAC SHA256 in our case)
- `typ`: Token type (JWT)

### Part 2: Payload (Base64 encoded) - Contains claims
```json
{
  "sub": "user@email.com",
  "role": "USER",
  "iat": 1685000000,
  "exp": 1685086400
}
```
- `sub` (subject): User email
- `role`: User role (USER or ADMIN)
- `iat` (issued at): Token creation timestamp
- `exp` (expiration): Token expiry timestamp

### Part 3: Signature
The signature is created by:
```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret_key
)
```
This signature ensures the token has not been tampered with.

---

## How JWT Authentication Works in Our Project

### Login Flow (Token Creation)
```
Client                                Server
  |                                      |
  |  POST /api/auth/login                |
  |  { email, password }                 |
  |------------------------------------->|
  |                                      |
  |                           AuthService.login()
  |                           AuthenticationManager.authenticate()
  |                           CustomUserDetailsService.loadUserByUsername()
  |                           PasswordEncoder.matches()
  |                                      |
  |                           JwtUtil.generateToken(email, role)
  |                           Token: { sub: email, role: USER, exp: ... }
  |                                      |
  |  { message, email, role, token }     |
  |<-------------------------------------|
```

### Authenticated Request Flow (Token Validation)
```
Client                                Server
  |                                      |
  |  GET /api/protected/resource         |
  |  Header: Authorization Bearer <token>|
  |------------------------------------->|
  |                                      |
  |  JwtAuthFilter.doFilterInternal()    |
  |    1. Extract "Authorization" header |
  |    2. Remove "Bearer " prefix        |
  |    3. JwtUtil.extractEmail(token)    |
  |    4. CustomUserDetailsService       |
  |       .loadUserByUsername(email)     |
  |    5. JwtUtil.isTokenValid()         |
  |       - Check email matches          |
  |       - Check token not expired      |
  |                                      |
  |    If valid: Set SecurityContext     |
  |    If invalid: Skip (return 401)     |
  |                                      |
  |  Controller processes request        |
  |<-------------------------------------|
```

---

## JWT Configuration in Our Project

### application.properties
```properties
# Secret key for signing tokens (base64 encoded)
jwt.secret=${JWT_SECRET}

# Token expiry time in milliseconds (24 hours = 86400000)
jwt.expiration=${JWT_EXPIRATION:86400000}

# HTTP header where token is sent by client
jwt.header=Authorization

# Token type prefix in the header
jwt.prefix=Bearer
```

### .env File Values
```
JWT_SECRET=f1f8a7839e3c2e7f1d4b056a2c7e9f4a3d1b2c3e4d5f6a7b8c9d0e1f2a3b4c5d
JWT_EXPIRATION=86400000
```

---

## Key Classes in Our Implementation

### 1. JwtUtil.java
Location: `backend/src/main/java/com/secureauthlab/backend/security/JwtUtil.java`

| Method | Purpose |
|--------|---------|
| `getSigningKey()` | Converts base64 secret to HMAC key |
| `generateToken(email, role)` | Creates signed JWT with claims |
| `extractEmail(token)` | Reads email from token payload |
| `extractRole(token)` | Reads role from token payload |
| `isTokenValid(token, userDetails)` | Validates token against user data |

### 2. JwtAuthFilter.java
Location: `backend/src/main/java/com/secureauthlab/backend/security/JwtAuthFilter.java`

- Extends `OncePerRequestFilter` (runs once per request)
- Checks `Authorization` header for `Bearer <token>` format
- Validates token and sets `SecurityContext` if valid
- If token is missing/invalid, request continues without authentication

### 3. SecurityConfig.java Filter Chain
```java
.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
```
The JWT filter runs before Spring's default authentication filter.

---

## Security Best Practices Followed

1. **Secret is in .env file** - Not hardcoded in code
2. **Token has expiration** - 24 hours, then client must login again
3. **Error messages are generic** - "Invalid email or password" instead of "Wrong password"
4. **Invalid tokens are silently ignored** - No stack trace exposed to client
5. **HS256 signing** - Secure HMAC algorithm

---

## Testing JWT with Postman

### Step 1: Login to get token
```
POST http://localhost:8080/api/auth/login
Body: { "email": "test@email.com", "password": "123456" }
```
Copy the `token` value from response.

### Step 2: Use token in next request
```
GET http://localhost:8080/api/protected/resource
Headers:
  Authorization: Bearer <paste-token-here>
```

### Step 3: Test expired token
Wait 24 hours or use an incorrect token to verify 401 response.