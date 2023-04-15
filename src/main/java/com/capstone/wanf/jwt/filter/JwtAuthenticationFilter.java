package com.capstone.wanf.jwt.filter;

import com.capstone.wanf.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.IncorrectClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// UsernamePasswordAuthenticationFilter 이전에 통과할 Filter
// 인증(Authentication)이 필요한 요청이 오면 요청의 헤더에서 AccessToken을 추출하고 정상 토큰인지 검사
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // Access Token 추출
        String accessToken = resolveToken(request);

        // 유효 기간만 제외하고 정상적인 토큰일 경우, Access Token을 재발급하고 로그인을 연장시킴과 동시에 다시 요청을 처리(Silent refresh)
        try { // 정상 토큰인지 검사
            if (accessToken != null && jwtTokenProvider.validateAccessToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Save authentication in SecurityContextHolder.");
            }
        } catch (IncorrectClaimException e) { // 잘못된 토큰일 경우
            SecurityContextHolder.clearContext();

            log.debug("Invalid JWT token.");

            response.sendError(403);
        } catch (UsernameNotFoundException e) { // 회원을 찾을 수 없을 경우
            SecurityContextHolder.clearContext();

            log.debug("Can't find user.");

            response.sendError(403);
        }

        filterChain.doFilter(request, response);
    }

    // HTTP Request 헤더로부터 토큰 추출
    public String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
