package org.example.securitystudy.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.example.securitystudy.user.UserPrincipal;
import org.example.securitystudy.user.UserPrincipalDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

public class JwtProvider {

    private final Key key;
    private final long accessTokenExpiredTime
    private final long refreshTokenExpiredTime;
    private final UserPrincipalDetailService customUserDetailsService;

    public JwtProvider(final String secretKey,
                       final long accessTokenExpiredTime,
                       final long refreshTokenExpiredTime,
                       final UserPrincipalDetailService customUserDetailsService) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiredTime = accessTokenExpiredTime;
        this.refreshTokenExpiredTime = refreshTokenExpiredTime;
        this.customUserDetailsService = customUserDetailsService;
    }

    public Token generateToken(final String subject) {
        return new Token(generateAccessToken(subject), generateRefreshToken(subject));
    }

    private String generateAccessToken(final String subject) {
        final Claims claims = Jwts.claims()
                .setSubject(subject);

        final Date now = new Date();
        final Date expiresIn = new Date(now.getTime() + accessTokenExpiredTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(key)
                .compact();
    }

    private String generateRefreshToken(final String subject) {
        final Claims claims = Jwts.claims()
                .setSubject(subject);

        final Date now = new Date();
        final Date expiresIn = new Date(now.getTime() + refreshTokenExpiredTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(final String token) {
        final Claims claims = parseClaims(token);
        final UserPrincipal customUserDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    }

    public String getUsername(final String token) {
        final Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public Claims parseClaims(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (final ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (final IllegalArgumentException ignored) {
            throw new JwtException(ignored.getMessage());
        }
    }

    public long getExpiration(final String accessToken) {
        final Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        final long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}
