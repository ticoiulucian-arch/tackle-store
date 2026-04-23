package ro.tacklestore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret:tackle-store-secret-key-change-in-production}")
    private String secret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long expirationMs; // 24h default

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(Algorithm.HMAC256(secret));
    }

    public String getUsername(String token) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getSubject();
    }

    public String getRole(String token) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getClaim("role").asString();
    }

    public boolean isValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}

