package com.capstone.wanf.auth.jwt.filter;

import com.capstone.wanf.auth.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.IncorrectClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 인증(Authentication)이 필요한 요청이 오면 요청의 헤더에서 AccessToken을 추출하고 정상 토큰인지 검사하는 Filter
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 유효 기간만 제외하고 정상적인 토큰일 경우, Access Token을 재발급하고 로그인을 연장시킴과 동시에 다시 요청을 처리(Silent refresh)
     * @param request
     * @return
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request);

        try {
            if (accessToken != null && jwtTokenProvider.validateAccessToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (IncorrectClaimException | UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();

            response.sendError(403);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 헤더에서 토큰 추출
     * @param httpServletRequest : 헤더에서 토큰 추출
     * @return
     */
    public String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
