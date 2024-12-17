# RESTFUL API for Food Order

Feel free to use & modify this API for creating food order.

## User Management

### Create User
Endpoint : POST /api/users

Request Header : None

Request Body:
```json
{
    "username" : "bintang.ginanjar",
    "password" : "password"
}
```

Response Body:
```json
{
    "status": true,
    "messages": "User registration success",
    "errors": null,
    "data": {
        "username": "bintang.ginanjar",
        "name": "Bintang Ginanjar"
    }
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
    "status": true,
    "messages": "User fetching success",
    "errors": null,
    "data": {
        "username": "bintang.ginanjar",
        "name": "Bintang Ginanjar"
    }
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
    "status": true,
    "messages": "User update success",
    "errors": null,
    "data": {
        "username": "bintang.ginanjar",
        "name": "Bintang Ginanjar"
    }
}
```

## Auth Management

### User Login
Endpoint : POST /api/auth/login

Request Header : None

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
    "status": null,
    "messages": null,
    "errors": null,
    "data": {
        "token": "05072151-1c79-48de-b1bb-2d5eaf6f1912",
        "expiredAt": 1734404530947
    }
}
```

### User Logout
Endpoint : DELETE /api/auth/logout

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
    "status": true,
    "messages": "User logout success",
    "errors": null,
    "data": null
}
```