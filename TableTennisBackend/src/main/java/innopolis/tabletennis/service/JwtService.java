package innopolis.tabletennis.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.*;
import io.jsonwebtoken.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.security.*;
import java.util.*;
import java.util.function.*;

@Service
public class JwtService {
    @Value("${jwt.SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${jwt.EXPIRATION_TIME}") // 86400000ms = 24h
    private long EXPIRATION_TIME;

    public String generateToken(UserDetails userDetails) {
        return generateToken(Collections.emptyMap(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // increase expiration time depending on roles
        int coef = 1;
        Object rolesObject = extraClaims.get("roles");
        if (rolesObject instanceof List<?>) {
            List<?> rolesList = (List<?>) rolesObject;
            if (!rolesList.isEmpty() && rolesList.get(0) instanceof String) {
                if (rolesList.contains("USER"))
                    coef = 31;
                if (rolesList.contains("LEADER"))
                    coef = 14;
                if (rolesList.contains("ADMIN"))
                    coef = 1;
            }
        }
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME * coef))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        if (extractExpiration(token) == null)
            throw new MalformedJwtException("Expiration date is not readable or not specified");
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

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
}
