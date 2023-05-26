package com.capstone.wanf.error.handler;

import com.capstone.wanf.error.errorcode.CommonErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.setCharacterEncoding("utf-8");

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode errorJson = mapper.createObjectNode();

        errorJson.put("timestamp", LocalDateTime.now().toString());
        errorJson.put("status", HttpStatus.FORBIDDEN.value());
        errorJson.put("code", CommonErrorCode.FORBIDDEN.name());
        errorJson.put("message", CommonErrorCode.FORBIDDEN.getMessage());

        response.getWriter().write(mapper.writeValueAsString(errorJson));
    }
}
