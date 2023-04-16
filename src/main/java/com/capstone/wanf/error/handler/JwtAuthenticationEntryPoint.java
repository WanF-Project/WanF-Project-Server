package com.capstone.wanf.error.handler;

import com.capstone.wanf.error.errorcode.CommonErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setCharacterEncoding("utf-8");

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode errorJson = mapper.createObjectNode();

        errorJson.put("timestamp", LocalDateTime.now().toString());
        errorJson.put("status", response.getStatus());
        errorJson.put("code", CommonErrorCode.UNAUTHORIZED.name());
        errorJson.put("message", CommonErrorCode.UNAUTHORIZED.getMessage());

        response.getWriter().write(mapper.writeValueAsString(errorJson));
    }
}
