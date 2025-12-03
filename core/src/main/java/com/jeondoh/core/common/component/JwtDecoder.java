package com.jeondoh.core.common.component;

import com.jeondoh.core.common.dto.JwtToken;
import com.jeondoh.core.common.exception.JwtDecodedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static com.jeondoh.core.common.util.StaticVariables.*;

@Component
public class JwtDecoder {

    public JwtDecoder(@Value("${jwt.secret}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    private final SecretKey secretKey;

    // JWT 디코딩
    public JwtToken decode(String headerToken) {
        String token = extractToken(headerToken);
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String memberId = claims.getSubject();
            String username = claims.get(NAME_CLAIM_KEY, String.class);
            String role = claims.get(ROLE_CLAIM_KEY, String.class);

            return JwtToken.of(memberId, username, role);

        } catch (SecurityException | MalformedJwtException e) {
            throw JwtDecodedException.invalidSignedException();
        } catch (ExpiredJwtException e) {
            throw JwtDecodedException.expiredException();
        } catch (UnsupportedJwtException e) {
            throw JwtDecodedException.unsupportedException();
        } catch (IllegalArgumentException e) {
            throw JwtDecodedException.illegalArgumentException();
        }
    }

    // 헤더에서 토큰 추출
    private String extractToken(String token) {
        if (token == null || !token.startsWith(AUTH_HEADER_PREFIX)) {
            throw JwtDecodedException.illegalArgumentException();
        }
        return token.substring(AUTH_HEADER_PREFIX.length());
    }

}
