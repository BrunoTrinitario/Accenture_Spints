package com.mindhub.order_service.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        //restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
        //    @Override
        //    public void handleError(ClientHttpResponse response) throws IOException {
        //        System.out.println("xdxdxdxdxdxdxd");
        //    }
        //});

        return restTemplate;
    }
}
