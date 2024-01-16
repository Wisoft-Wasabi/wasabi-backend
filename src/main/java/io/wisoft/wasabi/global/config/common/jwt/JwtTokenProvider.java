package io.wisoft.wasabi.global.config.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.wisoft.wasabi.domain.member.persistence.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;

    private final long accessTokenValidityInMilliseconds;

    private final String issuer;

    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt.token.secret-key}") final String secretKey,
                            @Value("${jwt.access-token.expire-length}") final long accessTokenValidityInMilliseconds,
                            @Value("${jwt.issuer}") final String issuer) {
        this.secretKey = secretKey;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.issuer = issuer;
        this.key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
    }

    public String createAccessToken(final Long memberId, final String name, final Role role, final boolean activation) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + this.accessTokenValidityInMilliseconds);

        return Jwts.builder()
            .issuer(this.issuer)
            .subject(this.issuer + "/members/" + memberId)
            .claim("memberId", memberId)
            .claim("memberName", name)
            .claim("memberRole", role)
            .claim("memberActivation", activation)
            .issuedAt(now)
            .expiration(validity)
            .signWith(this.key)
            .compact();
    }

    public Long decodeAccessToken(final String accessToken) {

        return Jwts.parser()
            .verifyWith(this.key)
            .build()
            .parseSignedClaims(accessToken)
            .getPayload()
            .get("memberId", Double.class)
            .longValue();
    }

    public Role decodeRole(final String accessToken) {
        final Claims claims = Jwts.parser()
            .verifyWith(this.key)
            .build()
            .parseSignedClaims(accessToken)
            .getPayload();

        return Role.valueOf(claims.get("memberRole", String.class));
    }
}
