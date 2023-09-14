package com.example.RedditClone.controller;

import com.example.RedditClone.dto.AutheticationResponse;
import com.example.RedditClone.dto.LoginRequest;
import com.example.RedditClone.dto.RefreshTokenRequest;
import com.example.RedditClone.dto.RegisterRequest;
import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.service.AuthService;
import com.example.RedditClone.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class);
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    private KafkaTemplate<String, String> kafkaTemplate;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        //return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AutheticationResponse login(@RequestBody LoginRequest loginRequest){
        AutheticationResponse a = authService.login(loginRequest);
        logger.info("User " + loginRequest.getUsername() + " logged in.");
        kafkaTemplate.send("redditCloneTopic","User " + loginRequest.getUsername() + " logged in at " + Instant.now());
        return a;

    }

    @PostMapping("refresh/token")
    public AutheticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
            refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
            logger.info("User" + refreshTokenRequest.getUsername() + "logged out.");
            kafkaTemplate.send("redditCloneTopic", "User " + refreshTokenRequest.getUsername() + " logged out at " + Instant.now());
            return new ResponseEntity<>("Refresh token deleted", HttpStatus.OK);

    }


}
