package com.leapfinance;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.function.Function;

public class JWTUtil {

    @Value("${secret}")
    private static String SECRET = "thisisasecret";
    @Value("${expiration}")
    private static long EXPIRATION = 86400000;

    public static String generateToken(String subject) {
        long timestamp = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    public static String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
