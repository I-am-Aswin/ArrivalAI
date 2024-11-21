# Arrival AI Login API Documentation


## User Register - /user/register

    Body : { "username": "<uname>", "password": "password" }
    Response : { "username": "<uname>", "password": "password" }

## User Login - /user/login

    Body : { "username": "<uname>", "password": "password" }
    Response : "Json Web Token"

## Test Page - /
    Response : "Hello World...!"