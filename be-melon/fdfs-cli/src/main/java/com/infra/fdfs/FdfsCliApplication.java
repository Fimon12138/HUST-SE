package com.infra.fdfs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.infra.fdfs.mapper")
public class FdfsCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(FdfsCliApplication.class, args);
	}

}
