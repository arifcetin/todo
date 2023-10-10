package com.example.proje.controllers;

import com.example.proje.entities.User;

import com.example.proje.response.AuthResponse;
import com.example.proje.security.JwtTokenProvider;
import com.example.proje.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                          UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody User loginUser){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginUser.getUserName(),loginUser.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userService.getOneUserByUserName(loginUser.getUserName());
        AuthResponse authResponse = new AuthResponse();
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);
        authResponse.setAccessToken(jwtToken);
        authResponse.setUserId(user.getUser_id());
        return authResponse;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse>  register(@RequestBody User registerUser){
        AuthResponse authResponse = new AuthResponse();
        if (userService.getOneUserByUserName(registerUser.getUserName()) != null){
            authResponse.setMessage("Kullanıcı adı zaten kullanılıyor");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        newUser.setUserName(registerUser.getUserName());
        newUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        userService.createUser(newUser);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                registerUser.getUserName(),registerUser.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        authResponse.setMessage("Kullanıcı başarıyla oluşturuldu.");
        authResponse.setAccessToken("Bearer "+ jwtToken);
        authResponse.setUserId(newUser.getUser_id());
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PutMapping("/forget")
    public AuthResponse forgetPassword(@RequestBody User user){
        AuthResponse authResponse = new AuthResponse();
        User foundUser = userService.getOneUserByUserName(user.getUserName());
        if (foundUser != null){
            foundUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.createUser(foundUser);
            authResponse.setMessage("Şifre başarıyla değiştirildi");
            return authResponse;
        }
        authResponse.setMessage("Kullanıcı adı eşleşmiyor");
        return authResponse;
    }

}
