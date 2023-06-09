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
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@Transactional(readOnly = true)
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

        // seconds -> milliseconds
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;

        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
    }

    // 시크릿 키 설정
    @Override
    public void afterPropertiesSet() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);

        signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    @Transactional
    public TokenResponse createToken(String email, String authorities) {     // 토큰 발급
        Long now = System.currentTimeMillis();

        // User.email(Principal)값과 User.role 값을 매개변수로 받아 사용
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(new Date(now + accessTokenValidityInMilliseconds))
                .setSubject("access-token")
                .claim(EMAIL_KEY, email)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        // refreshToken은 유효기간이 길고, 재발급 용도로만 사용하기 때문에 claims를 최소화
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


    // == 토큰으로부터 정보 추출 == //

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) { // Access Token
            return e.getClaims();
        }
    }

    // 토큰으로부터 인증 정보 객체인 UsernamePasswordAuthenticationToken을 반환
    public Authentication getAuthentication(String token) {
        String email = getClaims(token).get(EMAIL_KEY).toString();

        UserDetailsImpl userDetailsImpl = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetailsImpl, "", userDetailsImpl.getAuthorities());
    }

    public long getTokenExpirationTime(String token) {
        return getClaims(token).getExpiration().getTime();
    }


    // == 토큰 검증 == //

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

    // Filter에서 사용
    public boolean validateAccessToken(String accessToken) {
        try {
            if (redisService.getValues(accessToken) != null // NPE 방지
                    && redisService.getValues(accessToken).equals("logout")) { // 로그아웃 했을 경우
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

    // 재발급 검증 API에서 사용
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

}
