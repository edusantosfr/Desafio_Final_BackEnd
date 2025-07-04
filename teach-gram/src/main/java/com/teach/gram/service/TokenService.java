package com.teach.gram.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.teach.gram.dto.res.login.LoginResDTO;
import com.teach.gram.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${security.secret}")
    private String SECRET;

    public LoginResDTO generateToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        Instant expirationDate = getExpirationDate();

        String token = JWT.create()
                .withIssuer("3035teach/todo-teach")
                .withSubject(user.getUsername())
                .withExpiresAt(expirationDate)
                .sign(algorithm);

        return new LoginResDTO(
                "Bearer",
                token,
                expirationDate.toEpochMilli()
        );
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        String subject = JWT.require(algorithm)
                .withIssuer("3035teach/todo-teach")
                .build()
                .verify(token)
                .getSubject();

        return subject;
    }

    private Instant getExpirationDate() {

        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }
}
