package se.sowl.stitchapi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnivCertConfig {
    @Value("${univcert.api.key}")
    private String apiKey;

    @Bean
    public String univCertApiKey() {
        return apiKey;
    }
}