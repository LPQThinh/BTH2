package com.example.jwt_service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String encodedPassword = Base64.getEncoder()
                .encodeToString(
                        "123456".getBytes(StandardCharsets.UTF_8)
                );

        if ("admin".equals(request.getUserName())
                && encodedPassword.equals(request.getPassword())) {

            String token = jwtService.generateToken(
                    request.getUserName()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "idUser", 1,
                            "userName", request.getUserName(),
                            "token", token
                    )
            );
        }

        return ResponseEntity.status(401)
                .body("Username hoặc password không đúng");
    }
}
