package com.example.jwt_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthVerifyController {

    private final JwtService jwtService;

    public AuthVerifyController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<?> authenticate(
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing JWT token");
        }

        String token = authorization.substring(7);

        if (jwtService.validateToken(token)) {
            String userName = jwtService.getUserName(token);

            return ResponseEntity.ok(
                    "Token hợp lệ. Xin chào " + userName
            );
        }

        return ResponseEntity.status(401).body("JWT token không hợp lệ hoặc đã hết hạn");
    }
}
