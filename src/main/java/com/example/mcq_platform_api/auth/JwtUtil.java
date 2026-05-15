package com.example.mcq_platform_api.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret_key;

    public String generateToken(String username){
        return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis()+86400000))
        .signWith(SignatureAlgorithm.HS256,secret_key)

        .compact();
                
    }
    public String extractUsername(String token){
        return Jwts.parser()
        .setSigningKey(secret_key)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
