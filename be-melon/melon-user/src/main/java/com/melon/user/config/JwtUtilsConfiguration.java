package com.melon.user.config;

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
