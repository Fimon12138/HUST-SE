package com.melon.user.config;

import com.melon.common.tools.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfiguration {

    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator(0L, 1L);
    }

}
