package com.melon.message.config;

import com.melon.common.tools.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdConfiguration {

    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator(1L, 0L);
    }

}
