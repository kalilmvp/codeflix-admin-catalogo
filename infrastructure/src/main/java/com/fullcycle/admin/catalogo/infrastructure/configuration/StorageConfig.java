package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.GoogleStorageProperties;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;
import com.fullcycle.admin.catalogo.infrastructure.services.impl.GCStorageService;
import com.fullcycle.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author kalil.peixoto
 * @date 6/25/24 17:34
 * @email kalilmvp@gmail.com
 */
@Configuration
public class StorageConfig {

    @Bean(name = "storageService")
    @Profile({"development", "production"})
    public StorageService gcStorageService(final GoogleStorageProperties props,
                                           final Storage storage) {
        return new GCStorageService(props.getBucket(), storage);
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}
