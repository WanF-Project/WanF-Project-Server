package com.capstone.wanf.auth.jwt.provider;

import com.capstone.wanf.auth.jwt.domain.UserDetailsImpl;
import com.capstone.wanf.auth.jwt.dto.response.TokenResponse;
import com.capstone.wanf.auth.jwt.service.UserDetailsServiceImpl;
import com.capstone.wanf.auth.redis.service.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisService redisService;

    private static final String AUTHORITIES_KEY = "role";
    private static final String EMAIL_KEY = "email";

    private final String secretKey;
    private static Key signingKey;

    private final Long accessTokenValidityInMilliseconds;
    private final Long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(
            UserDetailsServiceImpl userDetailsService,
            RedisService redisService,
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-validity-in-seconds}") Long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") Long refreshTokenValidityInMilliseconds) {
        this.userDetailsService = userDetailsService;

        this.redisService = redisService;

        this.secretKey = secretKey;

        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;

        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
    }

    /**
     * Authentication 객체의 권한 정보를 이용해 토큰 생성
     * @param email
     * @param authorities : 권한 정보
     * @return TokenResponse : accessToken, refreshToken
     */
    public TokenResponse createToken(String email, String authorities) {
        Long now = System.currentTimeMillis();

        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(new Date(now + accessTokenValidityInMilliseconds))
                .setSubject("access-token")
                .claim(EMAIL_KEY, email)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(new Date(now + refreshTokenValidityInMilliseconds))
                .setSubject("refresh-token")
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * Access Token으로부터 Authentication 객체를 가져옴
     * @param accessToken
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String accessToken) {
        String email = getClaims(accessToken).get(EMAIL_KEY).toString();

        UserDetailsImpl userDetailsImpl = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetailsImpl, "", userDetailsImpl.getAuthorities());
    }

    public long getTokenExpirationTime(String token) {
        return getClaims(token).getExpiration().getTime();
    }


    /**
     * Refresh Token 검증
     * @param refreshToken
     * @return 유효 여부 (true: 유효, false: 만료, 또는 유효하지 않아 Exception 발생)
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(refreshToken);

            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        } catch (NullPointerException e) {
            log.error("JWT Token is empty.");
        }
        return false;
    }

    /**
     * Access Token 검증
     * @param accessToken 검증할 Access Token
     * @return 유효 여부 (true: 유효, false: 만료, 또는 유효하지 않음)
     */
    public boolean validateAccessToken(String accessToken) {
        try {
            if (redisService.getValues(accessToken) != null && redisService.getValues(accessToken).equals("logout")) {
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(accessToken);

            return true;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Access Token 만료 여부만 확인
     * @param accessToken
     * @return 만료 여부 (true: 만료, false: 유효)
     */
    public boolean validateAccessTokenOnlyExpired(String accessToken) {
        try {
            return getClaims(accessToken)
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Access Token 검증
     * @param accessToken
     * @return 검증 결과
     */
    public Claims getClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) { // Access Token
            return e.getClaims();
        }
    }

    @Override
    public void afterPropertiesSet() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);

        signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }
}
