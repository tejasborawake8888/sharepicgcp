package com.sharepic.user.service;

import com.sharepic.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long expirationDuration;

    public String generateToken(User user){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationDuration);
        byte[] secret = Decoders.BASE64.decode(jwtSecret);
        SecretKey signingKey = Keys.hmacShaKeyFor(secret);
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("Role",user.getRole().name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    public Claims parseToken(String token){
        byte[] secret = Decoders.BASE64.decode(jwtSecret);
        SecretKey signingKey = Keys.hmacShaKeyFor(secret);
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
