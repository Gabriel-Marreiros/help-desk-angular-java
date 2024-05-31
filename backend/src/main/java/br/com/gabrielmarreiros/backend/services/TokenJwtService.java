package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.exceptions.InvalidTokenException;
import br.com.gabrielmarreiros.backend.models.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenJwtService {
    @Value("${security.secret}")
    private String secret;
    private MacAlgorithm algorithm = Jwts.SIG.HS256;
    private String issuer = "Help Desk Angular & Java";

    public String generateToken(User user){

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("name", user.getName());
        claims.put("role", user.getRole().getTitle());
        claims.put("roleId", user.getRole().getId());
        claims.put("profilePicture", user.getProfilePicture());

        String token = Jwts.builder()
            .issuer(this.issuer)
            .subject(user.getEmail())
            .claims(claims)
            .signWith(this.getSecret(), this.algorithm)
            .compact();

        return token;
    }

    public String validateToken(String token) throws InvalidTokenException{
        JwtParser tokenParser = Jwts.parser()
                                    .verifyWith(this.getSecret())
                                    .build();

        try{
            String subject = tokenParser
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

            return subject;
        }
        catch(JwtException | IllegalArgumentException e){
            throw new InvalidTokenException("Token inválido.");
        }
    }

    private SecretKey getSecret(){
        byte[] secretByteArray = this.secret.getBytes();
        return Keys.hmacShaKeyFor(secretByteArray);
    }
}
