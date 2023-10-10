package com.example.proje.controllers;

import com.example.proje.dto.AuthDto;
import com.example.proje.entities.User;

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
    public AuthDto login(@RequestBody User loginUser){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginUser.getUserName(),loginUser.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userService.getOneUserByUserName(loginUser.getUserName());
        AuthDto authDto = new AuthDto();
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);
        authDto.setAccessToken(jwtToken);
        authDto.setUserId(user.getUser_id());
        return authDto;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthDto>  register(@RequestBody User registerUser){
        AuthDto authDto = new AuthDto();
        if (userService.getOneUserByUserName(registerUser.getUserName()) != null){
            authDto.setMessage("Kullanıcı adı zaten kullanılıyor");
            return new ResponseEntity<>(authDto, HttpStatus.BAD_REQUEST);
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

        authDto.setMessage("Kullanıcı başarıyla oluşturuldu.");
        authDto.setAccessToken("Bearer "+ jwtToken);
        authDto.setUserId(newUser.getUser_id());
        return new ResponseEntity<>(authDto, HttpStatus.CREATED);
    }

    @PutMapping("/forget")
    public AuthDto forgetPassword(@RequestBody User user){
        AuthDto authDto = new AuthDto();
        User foundUser = userService.getOneUserByUserName(user.getUserName());
        if (foundUser != null){
            foundUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.createUser(foundUser);
            authDto.setMessage("Şifre başarıyla değiştirildi");
            return authDto;
        }
        authDto.setMessage("Kullanıcı adı eşleşmiyor");
        return authDto;
    }

}
