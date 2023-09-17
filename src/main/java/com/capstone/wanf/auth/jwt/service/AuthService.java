package com.capstone.wanf.auth.jwt.service;

import com.capstone.wanf.auth.jwt.dto.response.TokenResponse;
import com.capstone.wanf.auth.jwt.provider.JwtTokenProvider;
import com.capstone.wanf.auth.redis.service.RedisService;
import com.capstone.wanf.user.dto.request.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsServiceImpl userDetailsService;

    private final RedisService redisService;

    private final String SERVER = "Server";

    /**
     * userDetials을 이용해 토큰 생성
     * Authenticaiton 객체를 생성해 SecurityContextHolder에 저장
     * JWT 토큰을 발급받아 반환한다
     * @param userRequest : email, userPassword
     * @return
     */
    @Transactional
    public TokenResponse login(UserRequest userRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userRequest.email());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userRequest.userPassword(),
                userDetails.getAuthorities());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        return generateToken(SERVER, authentication.getName(), getAuthorities(authentication));
    }

    /**
     * 토큰의 만료일자만 초과한 유효한 토큰인지 검사
     * @param requestAccessTokenInHeader : request header의 Authorization 값
     * @return true : 만료 토큰, false : 만료되지 않은 토큰
     */
    public boolean validate(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);

        return jwtTokenProvider.validateAccessTokenOnlyExpired(requestAccessToken);
    }

    /**
     * validate 메서드가 true 반환할 때만 사용
     * 토큰을 재발급한다
     * @param requestAccessTokenInHeader : request header의 Authorization의 값
     * @param requestRefreshToken : request header의 X-Refresh-Token 값
     * @return TokenResponse : 재발급된 AT, RT
     * @return null : 재발급 실패 -> 재로그인 필요
     */
    @Transactional
    public TokenResponse reissue(String requestAccessTokenInHeader, String requestRefreshToken) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);

        Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);

        String principal = getPrincipal(requestAccessToken);

        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);

        if (refreshTokenInRedis == null) {
            return null;
        }

        if (!jwtTokenProvider.validateRefreshToken(requestRefreshToken) || !refreshTokenInRedis.equals(requestRefreshToken)) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal);

            return null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authorities = getAuthorities(authentication);

        redisService.deleteValues("RT(" + SERVER + "):" + principal);

        TokenResponse tokenDto = jwtTokenProvider.createToken(principal, authorities);

        saveRefreshToken(SERVER, principal, tokenDto.getRefreshToken());

        return tokenDto;
    }

    /**
     * AT, RT 생성 및 Redis에 RT 저장
     * @param provider : Server
     * @param email : principal
     * @param authorities : 권한
     * @return TokenResponse : AT, RT
     */
    public TokenResponse generateToken(String provider, String email, String authorities) {
        if (redisService.getValues("RT(" + provider + "):" + email) != null) {
            redisService.deleteValues("RT(" + provider + "):" + email);
        }

        TokenResponse tokenResponse = jwtTokenProvider.createToken(email, authorities);

        saveRefreshToken(provider, email, tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    /**
     * Redis에 RT 저장
     * @param provider : Server
     * @param principal : email
     * @param refreshToken : RT
     */
    public void saveRefreshToken(String provider, String principal, String refreshToken) {
        redisService.setValuesWithTimeout("RT(" + provider + "):" + principal,
                refreshToken,
                jwtTokenProvider.getTokenExpirationTime(refreshToken));
    }

    /**
     * Authentication 객체에서 권한 정보만 추출
     * @param authentication : Authentication 객체
     * @return 권한 정보
     */
    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * AT에서 principal 추출
     * @param requestAccessToken : request header의 Authorization 값
     * @return principal
     */
    public String getPrincipal(String requestAccessToken) {
        return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
    }

    /**
     * request header의 Authorization 값에서 AT 추출
     * @param requestAccessTokenInHeader : request header의 Authorization 값
     * @return AT
     */
    public String resolveToken(String requestAccessTokenInHeader) {
        if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
            return requestAccessTokenInHeader.substring(7);
        }
        return null;
    }

    /**
     * 로그아웃 처리
     * @param requestAccessTokenInHeader : request header의 Authorization 값
     */
    @Transactional
    public void logout(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);

        String principal = getPrincipal(requestAccessToken);

        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);

        if (refreshTokenInRedis != null) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal);
        }

        long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();

        redisService.setValuesWithTimeout(requestAccessToken,
                "logout",
                expiration);
    }
}
