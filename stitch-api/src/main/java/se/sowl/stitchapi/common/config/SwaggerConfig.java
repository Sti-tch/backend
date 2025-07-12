package se.sowl.stitchapi.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stitch API")
                        .description("스티치 프로젝트 API 문서")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Stitch")
                                .email("kcw130502@gmail.com")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("로컬 개발 서버"));
    }

    /**
     * 모든 API에 공통 응답을 자동으로 추가
     */
    @Bean
    public OpenApiCustomizer globalResponseCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(operation -> {
                        ApiResponses responses = operation.getResponses();

                        // 200 응답이 없으면 추가 (GET, POST 등)
                        if (!responses.containsKey("200")) {
                            String httpMethod = pathItem.readOperationsMap().entrySet().stream()
                                    .filter(entry -> entry.getValue() == operation)
                                    .map(entry -> entry.getKey().toString())
                                    .findFirst().orElse("GET");

                            if ("GET".equals(httpMethod) || "POST".equals(httpMethod)) {
                                responses.addApiResponse("200", createSuccessResponse());
                            }
                        }

                        // 공통 에러 응답 추가
                        if (!responses.containsKey("400")) {
                            responses.addApiResponse("400", createErrorResponse(
                                    "잘못된 요청", "BAD_REQUEST", "잘못된 요청입니다."));
                        }

                        // 모든 API에 401, 403 추가 (로그인이 필요한 시스템이므로)
                        if (!responses.containsKey("401")) {
                            responses.addApiResponse("401", createErrorResponse(
                                    "인증 필요", "UNAUTHORIZED", "로그인이 필요합니다."));
                        }

                        if (!responses.containsKey("403")) {
                            responses.addApiResponse("403", createErrorResponse(
                                    "접근 권한 없음", "FORBIDDEN", "접근 권한이 없습니다."));
                        }

                        if (!responses.containsKey("500")) {
                            responses.addApiResponse("500", createErrorResponse(
                                    "서버 오류", "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."));
                        }
                    })
            );
        };
    }

    private ApiResponse createSuccessResponse() {
        return new ApiResponse()
                .description("성공")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType()
                                        .schema(new Schema<>()
                                                .example("""
                                                    {
                                                      "code": "SUCCESS",
                                                      "message": "성공",
                                                      "result": {}
                                                    }
                                                    """))));
    }

    private ApiResponse createErrorResponse(String description, String code, String message) {
        return new ApiResponse()
                .description(description)
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType()
                                        .schema(new Schema<>()
                                                .example(String.format("""
                                                    {
                                                      "code": "%s",
                                                      "message": "%s",
                                                      "result": null
                                                    }
                                                    """, code, message)))));
    }
}