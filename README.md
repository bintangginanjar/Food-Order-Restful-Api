# RESTFUL API for Food Order

Feel free to use & modify this API for creating food order

## User Management

### Create User
Endpoint : POST /api/users

Request Header : None

Request Body:
```json
{
    "username" : "username",
    "password" : "password",
    "name" : "name"
}
```

Response Body:
```json
{
    "username" : "username",    
    "name" : "name"
}
```

### Get Current User
Endpoint : GET /api/users/current

Request Header :

* X-API-TOKEN : Token (mandatory)

Request Body:
```json
{
    "username" : "username",
    "password" : "password",
    "name" : "name"
}
```

Response Body:
```json
{
    "username" : "username",    
    "name" : "name"
}
```

### Update User
Endpoint : PATCH /api/users/current

Request Header :

* X-API-TOKEN : Token (mandatory)

Request Body:
```json
{
    "username" : "username",
    "password" : "password"    
}
```

Response Body:
```json
{
    "username" : "username",    
    "name" : "name"
}
```