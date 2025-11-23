package com.example.basedato.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenProvider {

    // CAMBIA ESTA CLAVE SECRETA POR UNA MUCHO MÁS LARGA Y COMPLEJA EN PRODUCCIÓN
    private static final String SECRET_KEY = "RXN0YUVzVW5hQ2xhdmVTZWNyZXRhTXV5U2VndXJhUGFyYVR1UHJveWVjdG8xMjM0NTY3ODkwIQ==";
    private static final long JWT_EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24 horas

    // 1. Extrae un claim específico del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 2. Genera el token para un usuario (email)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // 3. Genera el token con claims adicionales (cuerpo del token)
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // Usa el email como 'subject'
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 4. Valida si el token es válido para un usuario
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 5. Extrae el nombre de usuario (email) del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // --- Métodos Auxiliares ---

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}