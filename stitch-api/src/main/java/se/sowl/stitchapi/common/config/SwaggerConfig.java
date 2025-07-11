package se.sowl.stitchapi.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
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
}
