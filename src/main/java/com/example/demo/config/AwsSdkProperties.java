package com.example.demo.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
@ConfigurationProperties(prefix = "aws.sdk")
@Setter
public class AwsSdkProperties {
    private String region;

    public Region getRegion() {
        return Region.of(this.region);
    }
}
