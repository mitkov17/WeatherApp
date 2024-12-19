package com.mitkov.weatherapp.WeatherApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mitkov.weatherapp.WeatherApp.entities.Role;
import com.mitkov.weatherapp.WeatherApp.entities.UserClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt-secret}")
    private String secret;

    public String generateToken(String username, Role role) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withClaim("role", role.name())
                .withIssuedAt(new Date())
                .withIssuer("WeatherApp")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public UserClaims validateTokenAndRetrieveClaims(String token) throws JWTVerificationException {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("WeatherApp")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        String username = jwt.getClaim("username").asString();
        String role = jwt.getClaim("role").asString();

        return new UserClaims(username, role);
    }

}
