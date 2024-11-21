package org.broz.arrivalai.controllers;

import org.broz.arrivalai.models.LoginRequest;
import org.broz.arrivalai.models.User;
import org.broz.arrivalai.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService( UserService userService ) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest user) {
        return userService.authorize( user );
    }

    @PostMapping("register")
    public ResponseEntity<LoginRequest> register(@RequestBody LoginRequest user) {
        return userService.register( user );
    }
}
