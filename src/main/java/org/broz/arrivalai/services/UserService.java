package org.broz.arrivalai.services;

import org.broz.arrivalai.dao.UserRepo;
import org.broz.arrivalai.models.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.broz.arrivalai.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
public class UserService {

    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<String> authorize(LoginRequest user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( user.getUsername(), user.getPassword()));

        if( authentication.isAuthenticated() ) {
            User currentUser = (User) authentication.getPrincipal();
            return new ResponseEntity<>(jwtService.generateJwToken(currentUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity<LoginRequest> register(LoginRequest currentUser) {
        User user = new User();
        user.setUsername(currentUser.getUsername());
        user.setPassword( passwordEncoder.encode(currentUser.getPassword()) );

        user = userRepo.save( user );
        return new ResponseEntity<>( new LoginRequest(user.getUsername(), user.getPassword()), HttpStatus.CREATED);
    }
}
