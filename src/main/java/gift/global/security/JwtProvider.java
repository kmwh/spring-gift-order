package gift.global.security;

import gift.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private final SecretKey key;
    private final long tokenValidityTime;

    public JwtProvider(
        @Value("${jwt.secret}") String rawSecret,
        @Value("${jwt.token-validity-time}") long tokenValidityTime
    ) {
        this.key = Keys.hmacShaKeyFor(rawSecret.getBytes());
        this.tokenValidityTime = tokenValidityTime;
    }

    public String createToken(Member member) {
        return Jwts.builder()
            .subject(member.getId().toString())
            .expiration(Date.from(Instant.now().plusSeconds(tokenValidityTime)))
            .signWith(key)
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public Long getMemberId(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }
}
