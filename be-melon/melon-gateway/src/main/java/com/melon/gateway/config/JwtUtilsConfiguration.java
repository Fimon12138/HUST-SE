package com.melon.gateway.config;

import com.melon.auth.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtUtilsConfiguration {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

}
