package com.agrihusac.SysInvent.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
public class Cors {

    private String allowOrigins;
    private String allowMethods;
    private String allowHeaders;
    private String exposedHeaders;
    private long maxAge;
    private boolean allowCredentials;
    private String mapping;
}
