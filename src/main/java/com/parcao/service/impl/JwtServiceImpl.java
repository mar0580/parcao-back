package com.parcao.service.impl;

import com.parcao.service.IJwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements IJwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";

    @Value("${parcao.app.jwtSecret:parcao-local-dev-secret-change-me-2026-very-long-key-for-hs512}")
    private String jwtSecret;

    @Value("${parcao.app.jwtExpirationMs:86400000}")
    private long jwtExpirationMs;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        // Garante que a chave tenha pelo menos 64 bytes para HS512
        String paddedSecret = jwtSecret;
        while (paddedSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
            paddedSecret = paddedSecret + paddedSecret;
        }
        this.signingKey = Keys.hmacShaKeyFor(paddedSecret.getBytes(StandardCharsets.UTF_8));
        logger.info("JwtService inicializado com expiração de {} ms", jwtExpirationMs);
    }

    @Override
    public String generateToken(Authentication auth) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Gerando token JWT", correlationId);

        UserDetails user = (UserDetails) auth.getPrincipal();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        logger.info("[correlationId={}] Token JWT gerado com sucesso para usuário: {}", correlationId, user.getUsername());
        return token;
    }

    @Override
    public String extractUsername(String token) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Extraindo username do token", correlationId);

        String username = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        logger.info("[correlationId={}] Username extraído: {}", correlationId, username);
        return username;
    }

    @Override
    public boolean validateToken(String authToken) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Validando token JWT", correlationId);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(authToken);

            logger.info("[correlationId={}] Token JWT válido", correlationId);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            logger.error("[correlationId={}] Assinatura JWT inválida: {}", correlationId, e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("[correlationId={}] Token JWT malformado: {}", correlationId, e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("[correlationId={}] Token JWT expirado: {}", correlationId, e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("[correlationId={}] Token JWT não suportado: {}", correlationId, e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("[correlationId={}] Claims JWT vazias: {}", correlationId, e.getMessage());
        }

        logger.info("[correlationId={}] Token JWT inválido", correlationId);
        return false;
    }
}
