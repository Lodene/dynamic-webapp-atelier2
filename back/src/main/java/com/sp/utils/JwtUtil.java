package com.sp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    // Clé secrète pour la signature du token JWT
    private static final String SECRET_KEY = "your_secret_key";

    /**
     * Crée un token JWT pour un sujet donné avec une durée d'expiration.
     *
     * @param subject     Sujet du token
     * @param expiration  Date d'expiration du token
     * @return            Token JWT généré
     */
    // Méthode pour générer un token JWT
    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 86400000); // Expiration dans 24 heures

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, "yourSecretKey") // Remplacez "yourSecretKey" par votre clé secrète
                .compact();
    }

    /**
     * Vérifie si un token JWT est valide.
     *
     * @param token  Token JWT à vérifier
     * @return       Vrai si le token est valide, faux sinon
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Récupère le sujet (username) à partir d'un token JWT.
     *
     * @param token  Token JWT à décoder
     * @return       Sujet du token (username)
     */
    public static String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}