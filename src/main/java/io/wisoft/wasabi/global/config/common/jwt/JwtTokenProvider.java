package io.wisoft.wasabi.global.config.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.wisoft.wasabi.domain.member.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.issuer}")
    private String issuer;

    public String createAccessToken(final Long memberId, final String name, final Role role, final boolean activation) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + this.accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setIssuer(this.issuer)
                .setSubject(this.issuer + "/members/" + memberId)
                .claim("memberId", memberId)
                .claim("memberName", name)
                .claim("memberRole", role)
                .claim("memberActivation", activation)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public Long decodeAccessToken(final String accessToken) {

        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(accessToken)
                .getBody()
                .get("memberId", Long.class);
    }

    public Role decodeRole(final String accessToken) {
       final Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(accessToken)
                .getBody();

        return Role.valueOf(claims.get("memberRole", String.class));
    }
}
