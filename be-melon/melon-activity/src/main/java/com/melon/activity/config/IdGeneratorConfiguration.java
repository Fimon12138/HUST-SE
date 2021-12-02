package com.melon.activity.config;

import com.melon.common.tools.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfiguration {

    private static final Long datacenterId = 0L;

    private static final Long workerId = 2L;

    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator(datacenterId, workerId);
    }

}
