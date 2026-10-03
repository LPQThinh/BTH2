package com.example.jwt_service;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (path.equals("/auth") || path.equals("/greeting")) {

            String authorization =
                    request.getHeader("Authorization");

            if (authorization == null
                    || !authorization.startsWith("Bearer ")) {

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Missing JWT token");
                return;
            }

            String token = authorization.substring(7);

            if (!jwtService.validateToken(token)) {

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(
                        "JWT token không hợp lệ hoặc đã hết hạn"
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
