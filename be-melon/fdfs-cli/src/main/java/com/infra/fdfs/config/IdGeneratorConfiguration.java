package com.infra.fdfs.config;

import com.melon.common.tools.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IdGenerator配置类
 *
 * @author: Fimon
 **/
@Configuration
public class IdGeneratorConfiguration {

  @Bean
  public IdGenerator idGenerator() {
    return new IdGenerator(0L, 0L);
  }


}
