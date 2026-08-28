package com.parking.app.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET =
            "parking-app-secret-key";

    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(
                        SignatureAlgorithm.HS256,
                        SECRET)
                .compact();
    }
}
