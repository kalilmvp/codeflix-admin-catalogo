package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.GoogleCloudProperties;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.GoogleStorageProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author kalil.peixoto
 * @date 6/24/24 21:02
 * @email kalilmvp@gmail.com
 */
@Configuration
@Profile({"development", "production"})
public class GoogleCloudConfig {

    @Bean
    @ConfigurationProperties("google.cloud")
    public GoogleCloudProperties googleCloudProperties() {
        return new GoogleCloudProperties();
    }

    @Bean
    @ConfigurationProperties("google.cloud.storage.catalogo-video")
    public GoogleStorageProperties googleStorageProperties() {
        return new GoogleStorageProperties();
    }
}
