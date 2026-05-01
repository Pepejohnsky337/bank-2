package com.bank.account.config;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {
            @Override
            public Exception decode(String methodKey, Response response) {
                if (response.status() == 404) {
                    try {
                        final String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                        return new EntityNotFoundException(body);
                    } catch (IOException e) {
                        return new EntityNotFoundException("Resource not found");
                    }
                }
                return new Exception("Service error");
            }
        };
    }
}
