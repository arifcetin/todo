package com.example.proje.controllers;

import com.example.proje.dto.AuthDto;
import com.example.proje.dto.PasswordTokenDto;
import com.example.proje.dto.UserDto;
import com.example.proje.entities.PasswordToken;
import com.example.proje.entities.User;

import com.example.proje.security.JwtTokenProvider;
import com.example.proje.services.PasswordTokenService;
import com.example.proje.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender mailSender;
    private ModelMapper modelMapper;
    private PasswordTokenService passwordTokenService;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                          UserService userService, PasswordEncoder passwordEncoder, JavaMailSender mailSender,
                          ModelMapper modelMapper, PasswordTokenService passwordTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.modelMapper = modelMapper;
        this.passwordTokenService = passwordTokenService;
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
        newUser.setEmail(registerUser.getEmail());
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

    @PostMapping("/getCode")
    public ResponseEntity<String> getCode(@RequestBody Map<String, String> request){
        User foundUser = userService.getOneUserByEmail(request.get("email"));
        if (foundUser != null){
            String token = passwordTokenService.createPasswordToken(foundUser);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("arif.cettin@gmail.com");
            message.setTo(request.get("email"));
            message.setSubject("Şifre Değiştirme");
            message.setText(token);
            mailSender.send(message);
            return new ResponseEntity<>("Doğrulama kodu gönderildi", HttpStatus.OK);
        }
        return new ResponseEntity<>("Doğru mail adresi giriniz", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/getCode/setPassword")
    public ResponseEntity<String> setPassword(@RequestBody PasswordTokenDto passwordTokenDto){
        PasswordToken token = passwordTokenService.findTokenByToken(passwordTokenDto.getToken());
        if(token == null){
            return new ResponseEntity<>("Doğrulama kodu yanlış girildi", HttpStatus.BAD_REQUEST);
        } else if (passwordTokenService.validateToken(token)) {
            return new ResponseEntity<>("Doğrulama kodunun kullanım süresi dolmuştur", HttpStatus.REQUEST_TIMEOUT);
        }
        User foundUser = modelMapper.map(userService.getOneUser(token.getUser().getUser_id()), User.class);
        foundUser.setPassword(passwordEncoder.encode(passwordTokenDto.getPassword()));
        return new ResponseEntity<>("Şifre başarıyla değiştirildi", HttpStatus.OK);
    }


}
