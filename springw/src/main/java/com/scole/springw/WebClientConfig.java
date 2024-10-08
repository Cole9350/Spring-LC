package com.scole.springw;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient graphQLWebClient() {
        return WebClient.builder()
                .baseUrl("https://leetcode.com")
                .build();
    }
}
