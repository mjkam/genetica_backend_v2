package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AwsSdkConfig {
    private final AwsSdkProperties awsSdkProperties;

    @Bean
    public S3Client s3Client() {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        return S3Client.builder()
                .region(awsSdkProperties.getRegion())
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
