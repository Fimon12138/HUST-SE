package com.melon.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class SwaggerResourceConfiguration implements SwaggerResourcesProvider {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> list = new ArrayList<>();
        discoveryClient.getServices().stream().filter(s -> !s.equals(applicationName)).forEach(
                service -> {
                    String temp = service.equals("fdfs-cli") ? "fdfs" :
                            service.split("-")[1];
                    list.add(swaggerResource(service, "/" + temp + "/v2/api-docs", "v1"));
                }
        );
        return list;
    }

    private SwaggerResource swaggerResource(String name, String url, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setUrl(url);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}
