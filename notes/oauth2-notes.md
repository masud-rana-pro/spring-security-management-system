# OAuth2 Notes - Secure Auth Lab

This document explains OAuth2 authentication, how it works, and how it will be implemented in this project.

---

## What is OAuth2?

OAuth2 is an authorization protocol that allows users to grant third-party applications access to their resources without sharing their password. It is commonly used for "Login with Google" or "Login with GitHub" features.

### Key Concepts

| Term | Meaning |
|------|---------|
| **Resource Owner** | The user who owns the account |
| **Client** | Our application (Secure Auth Lab) |
| **Authorization Server** | Google, GitHub (where user authenticates) |
| **Resource Server** | Google, GitHub (where user data is stored) |
| **Access Token** | Temporary token to access user data |
| **Refresh Token** | Token to get new access tokens |

---

## OAuth2 Flow (Authorization Code Flow)

```
User (Browser)                    Our App                      Google/GitHub
      |                              |                              |
      |  Click "Login with Google"   |                              |
      |----------------------------->|                              |
      |                              |                              |
      |  302 Redirect to Google      |                              |
      |  client_id, redirect_uri,    |                              |
      |  scope, response_type=code   |                              |
      |<-----------------------------|                              |
      |                              |                              |
      |  User sees Google login page |                              |
      |  Enters email and password   |                              |
      |---------------------------------------------------------->|
      |                              |                              |
      |  User grants permission      |                              |
      |---------------------------------------------------------->|
      |                              |                              |
      |  Authorization Code          |                              |
      |<-----------------------------------------------------------|
      |                              |                              |
      |  User redirected to our app  |                              |
      |  with ?code=abc123           |                              |
      |----------------------------->|                              |
      |                              |                              |
      |                   Our backend exchanges code for token      |
      |                   POST /token                               |
      |                     grant_type=authorization_code           |
      |                     code=abc123                             |
      |                     client_id + client_secret               |
      |---------------------------------------------------------->|
      |                              |                              |
      |                   Access Token + Refresh Token              |
      |<-----------------------------------------------------------|
      |                              |                              |
      |                   Fetch user info with access token         |
      |                   GET /userinfo                             |
      |---------------------------------------------------------->|
      |                              |                              |
      |                   User email, name, avatar                  |
      |<-----------------------------------------------------------|
      |                              |                              |
      |  Create/local account + JWT  |                              |
      |  Response: { token, email }  |                              |
      |<-----------------------------|                              |
```

---

## OAuth2 Configuration Required

### For Google Login

You need to create credentials at [Google Cloud Console](https://console.cloud.google.com):

1. Go to Google Cloud Console
2. Create a new project or select existing
3. Go to **APIs & Services > Credentials**
4. Create **OAuth 2.0 Client ID**
5. Set Application Type: **Web Application**
6. Add Redirect URI: `http://localhost:8080/login/oauth2/code/google`
7. Copy **Client ID** and **Client Secret**

### For GitHub Login

You need to register an OAuth app at [GitHub Developer Settings](https://github.com/settings/developers):

1. Go to GitHub Settings > Developer Settings
2. Click **OAuth Apps > New OAuth App**
3. Set:
   - Application Name: `Secure Auth Lab`
   - Homepage URL: `http://localhost:8080`
   - Authorization Callback URL: `http://localhost:8080/login/oauth2/code/github`
4. Copy **Client ID** and **Client Secret**

---

## OAuth2 Dependencies Already in pom.xml

Our project already has the required dependencies:

```xml
<!-- OAuth2 Client -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security-oauth2-client</artifactId>
</dependency>
```

---

## OAuth2 Application Properties (To Be Added)

In `.env`, add these values when ready:

```properties
# Google OAuth2
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# GitHub OAuth2
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret
```

In `application.properties`:

```properties
# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=email,profile

# GitHub OAuth2
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=user:email,read:user
```

---

## How Our App Will Handle OAuth2 Login

### Step 1: User clicks "Login with Google"
- User is redirected to Google's OAuth2 consent screen
- After approval, Google redirects back to our app

### Step 2: Custom OAuth2UserService
We need to create a service that handles the OAuth2 callback:

```java
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1. Fetch user info from Google/GitHub
        // 2. Check if user exists in our database
        // 3. If not, create a new user with GOOGLE/GITHUB provider
        // 4. Return OAuth2User wrapped in our CustomUserDetails
    }
}
```

### Step 3: JWT Token Generation
After OAuth2 login, the user gets a JWT token same as normal login:
- The frontend receives the JWT token
- All subsequent API calls use the same `Authorization: Bearer <token>` header

### Step 4: Merge Accounts
- If a user registers with email first (LOCAL provider), they can later link Google/GitHub
- The `AuthProvider` enum already supports: `LOCAL`, `GOOGLE`, `GITHUB`

---

## AuthProvider Enum (Already Created)

```java
public enum AuthProvider {
    LOCAL,    // Normal email/password registration
    GOOGLE,   // Login via Google OAuth2
    GITHUB    // Login via GitHub OAuth2
}
```

The `User` entity already has a `provider` field to track this.

---

## Benefits of OAuth2

| Benefit | Description |
|---------|-------------|
| No Password | Users don't need to remember another password |
| Security | Google/GitHub handle authentication securely |
| Profile Data | Get user name, email, avatar automatically |
| Trust | Users trust Google/GitHub more than a new site |
| Less Code | No need to implement password reset, email verification |

---

## Implementation Status

| Feature | Status |
|---------|--------|
| OAuth2 Dependencies in pom.xml | ✅ Added |
| AuthProvider Enum (LOCAL, GOOGLE, GITHUB) | ✅ Created |
| Google OAuth2 Config in application.properties | ❌ Not yet |
| GitHub OAuth2 Config in application.properties | ❌ Not yet |
| CustomOAuth2UserService | ❌ Not created |
| OAuth2 Success Handler | ❌ Not created |
| Frontend Login Buttons | ❌ Not created |
| Environment Variables in .env | ❌ Not yet |

OAuth2 implementation will be done in a later step after Role-Based Authorization.