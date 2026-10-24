package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    public String generateToken(UsuarioEntity usuario,
                               Integer rolId,
                               String nombreRol,
                               List<Map<String, Object>> modulos) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationMilliseconds);

        var builder = Jwts.builder()
                .setSubject(usuario.getUsuario())
                .claim("usuarioId", usuario.getUsuarioId())
                .claim("nombre", construirNombre(usuario))
                .claim("email", usuario.getEmail())
                .claim("rolId", rolId)
                .claim("nombreRol", nombreRol)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration);

        builder.claim("modulos", modulos);

        return builder
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String construirNombre(UsuarioEntity usuario) {
        return Stream.of(
                        usuario.getNombres(),
                        usuario.getApePaterno())
                .filter(valor -> valor != null && !valor.isBlank())
                .map(String::trim)
                .collect(Collectors.joining(" "));
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
