package com.capstone.wanf.config;

import com.capstone.wanf.common.response.FailureResponseBody;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("WanF API Document")
                .version("v0.0.1")
                .description("WanF 문서")
                .contact(new Contact().name("WanF-Project").url("https://github.com/WanF-Project"))
                .license(new License().name("MIT License").url("https://github.com/WanF-Project/WanF-Project-Server/blob/main/LICENSE"));

        String authName = "JWT token";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(authName);
        Components components = new Components()
                .addSecuritySchemes(
                        authName,
                        new SecurityScheme()
                                .name(authName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")
                                .description("Access Token(JWT) 토큰을 입력해주세요.(Bearer 안붙여도됨)")
                );

        Map<String, ApiResponse> responses = getResponses();

        for (String key : responses.keySet()) {
            components.addResponses(key, responses.get(key));
        }

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(info);
    }

    private Map<String, ApiResponse> getResponses() {
        ApiResponse noContent, badRequest, unauthorized, forbidden, notFound, internalServerError;
        var schema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(FailureResponseBody.class)).schema;

        noContent = new ApiResponse()
                .description("데이터 없음")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(null)
                        )
                );

        badRequest = new ApiResponse()
                .description("잘못된 요청입니다.")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );

        unauthorized = new ApiResponse()
                .description("인증을 할 수 없습니다.(토큰 없음, 만료된 토큰, 잘못된 토큰 ...)")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );

        forbidden = new ApiResponse()
                .description("접근할 수 없습니다.")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );

        notFound = new ApiResponse()
                .description("데이터를 찾을 수 없습니다.")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );

        internalServerError = new ApiResponse()
                .description("서버 오류(관리자 문의)")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );


        return Map.of(
                "204", noContent,
                "400", badRequest,
                "401", unauthorized,
                "403", forbidden,
                "404", notFound,
                "500", internalServerError
        );
    }
}
