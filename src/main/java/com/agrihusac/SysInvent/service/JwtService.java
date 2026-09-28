package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final Key key;
    private final long expirationMilliseconds;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMilliseconds = expirationMilliseconds;
    }

    public String generateToken(UsuarioEntity usuario, List<Map<String, Object>> roles) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationMilliseconds);

        return Jwts.builder()
                .setSubject(usuario.getUsuario())
                .claim("usuarioId", usuario.getUsuarioId())
                .claim("email", usuario.getEmail())
                .claim("roles", roles)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
