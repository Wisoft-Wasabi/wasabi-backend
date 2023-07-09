package io.wisoft.wasabi.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.wisoft.wasabi.domain.member.persistence.Member;
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

    public String createAccessToken(final String payload) {
        return createToken(payload, accessTokenValidityInMilliseconds);
    }

    public String createToken(final String payload, final long expireLength) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String createMemberToken(final Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setIssuer(this.issuer)
                .setSubject(this.issuer + "/members/" + member.getId())
                .claim("memberId", member.getId())
                .claim("memberName", member.getName())
                .claim("memberRole", member.getRole())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

}
