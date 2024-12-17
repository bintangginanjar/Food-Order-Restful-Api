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
## Profile Management

### Create Profile
Endpoint : POST /api/profiles

Request Header :
* X-API-TOKEN : Token (mandatory)

Request Body:
```json
{
    "firstname" : "Bintang",
    "lastname" : "Ginanjar",
    "email" : "email@mail.com",
    "address" : "address",
    "phoneNumber" : "1234567890",
    "city" : "Bandung",
    "province" : "West Java",
    "postalCode" : "40254"

}
```

Response Body:
```json
{
    "status": true,
    "messages": "Profile registration success",
    "errors": null,
    "data": {
        "id": 1,
        "firstname": "Bintang",
        "lastname": "Ginanjar",
        "email": "email@mail.com",
        "address": "address",
        "phoneNumber": "1234567890",
        "city": "Bandung",
        "province": "West Java",
        "postalCode": "40254"
    }
}
```

### Update Profile
Endpoint : PATCH /api/profiles

Request Header :
* X-API-TOKEN : Token (mandatory)

Request Body:
```json
{
    "firstname" : "Bintang",
    "lastname" : "Ginanjar",
    "email" : "email@mail.com",
    "address" : "address",
    "phoneNumber" : "1234567890",
    "city" : "Bandung",
    "province" : "West Java",
    "postalCode" : "40254"
}
```

Response Body:
```json
{
    "status": true,
    "messages": "Profile update success",
    "errors": null,
    "data": {
        "id": 1,
        "firstname": "Bintang",
        "lastname": "Ginanjar",
        "email": "email@mail.com",
        "address": "address",
        "phoneNumber": "1234567890",
        "city": "Bandung",
        "province": "West Java",
        "postalCode": "40254"
    }
}
```

## Category Management

### Create Category
Endpoint : POST /api/categories

Request Header :
* X-API-TOKEN : Token (mandatory)

Request Body:
```json
{
    "name" : "Side Dish"    

}
```

Response Body:
```json
{
    "status": true,
    "messages": "Category registration success",
    "errors": null,
    "data": {
        "id": 26,
        "name": "Side Dish"
    }
}
```

### Get Category
Endpoint : GET /api/category/{categoryId}

Request Header :
* X-API-TOKEN : Token (mandatory)

Request Body: None

Response Body:
```json
{
    "status": true,
    "messages": "Category fetching success",
    "errors": null,
    "data": {
        "id": 26,
        "name": "Side Dish"
    }
}
```

### Update Category
Endpoint : PUT /api/category/{categoryId}

Request Header :
* X-API-TOKEN : Token (mandatory)

Request Body:
```json
{
    "id": "26",
    "name": "Appetizer"    
}
```

Response Body:
```json
{
    "status": true,
    "messages": "Category update success",
    "errors": null,
    "data": {
        "id": 26,
        "name": "Appetizer"
    }
}
```