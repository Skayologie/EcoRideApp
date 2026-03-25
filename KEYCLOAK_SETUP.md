# Keycloak Configuration Guide

## Step-by-Step Setup

### 1. Access Keycloak Admin Console
- URL: http://localhost:8080
- Username: `admin`
- Password: `admin`

### 2. Create Realm

1. Click on the dropdown in the top-left (currently showing "Master")
2. Click "Create Realm"
3. Name: `edoride`
4. Click "Create"

### 3. Create Client

1. Go to "Clients" in the left menu
2. Click "Create client"
3. Configure:
   - Client ID: `edoride-client`
   - Client Protocol: `openid-connect`
   - Click "Next"
4. Capability config:
   - Client authentication: OFF (public client)
   - Authorization: OFF
   - Standard flow: ON
   - Direct access grants: ON
   - Click "Next"
5. Login settings:
   - Valid redirect URIs: `http://localhost:8888/*`
   - Web origins: `http://localhost:8888`
   - Click "Save"

### 4. Create Realm Roles

1. Go to "Realm roles" in the left menu
2. Click "Create role"
3. Create three roles:
   - Role name: `PASSENGER`
   - Role name: `DRIVER`
   - Role name: `ADMIN`

### 5. Create Test Users

#### Driver User
1. Go to "Users" in the left menu
2. Click "Add user"
3. Configure:
   - Username: `testdriver`
   - Email: `driver@edoride.com`
   - First name: `Jean`
   - Last name: `Dupont`
   - Email verified: ON
   - Click "Create"
4. Go to "Credentials" tab
   - Click "Set password"
   - Password: `password`
   - Temporary: OFF
   - Click "Save"
5. Go to "Role mapping" tab
   - Click "Assign role"
   - Select `DRIVER`
   - Click "Assign"

#### Passenger User
1. Click "Add user"
2. Configure:
   - Username: `testpassenger`
   - Email: `passenger@edoride.com`
   - First name: `Marie`
   - Last name: `Martin`
   - Email verified: ON
   - Click "Create"
3. Go to "Credentials" tab
   - Click "Set password"
   - Password: `password`
   - Temporary: OFF
   - Click "Save"
4. Go to "Role mapping" tab
   - Click "Assign role"
   - Select `PASSENGER`
   - Click "Assign"

#### Admin User
1. Click "Add user"
2. Configure:
   - Username: `testadmin`
   - Email: `admin@edoride.com`
   - First name: `Admin`
   - Last name: `User`
   - Email verified: ON
   - Click "Create"
3. Go to "Credentials" tab
   - Click "Set password"
   - Password: `password`
   - Temporary: OFF
   - Click "Save"
4. Go to "Role mapping" tab
   - Click "Assign role"
   - Select `ADMIN`
   - Click "Assign"

### 6. Verify Configuration

Test token generation:

```bash
curl -X POST http://localhost:8080/realms/edoride/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=edoride-client" \
  -d "username=testdriver" \
  -d "password=password" \
  -d "grant_type=password"
```

Expected response should include:
- `access_token`
- `refresh_token`
- `token_type: Bearer`

### 7. Optional: Configure Token Lifespan

1. Go to "Realm settings" → "Tokens" tab
2. Adjust:
   - Access Token Lifespan: 5 minutes (default)
   - Refresh Token Lifespan: 30 minutes
   - Click "Save"

## Troubleshooting

### Issue: Cannot get token
- Verify realm name is exactly `edoride`
- Verify client ID is `edoride-client`
- Verify user credentials are correct
- Check that "Direct access grants" is enabled on the client

### Issue: Token validation fails in services
- Verify issuer-uri in application.yml matches: `http://localhost:8080/realms/edoride`
- Check that services can reach Keycloak (network connectivity)

### Issue: Roles not in token
- Verify roles are assigned to users
- Check client scope configuration includes roles
