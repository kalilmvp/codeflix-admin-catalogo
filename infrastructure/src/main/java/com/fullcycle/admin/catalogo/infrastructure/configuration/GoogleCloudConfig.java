package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.GoogleCloudProperties;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.GoogleStorageProperties;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.threeten.bp.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

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

    @Bean
    public Credentials credentials(final GoogleCloudProperties props) throws IOException {
        final var json = Base64.getDecoder().decode(Objects.requireNonNull(props.getCredentials().replaceAll("\\s", "")));

        return GoogleCredentials.fromStream(new ByteArrayInputStream(json));
    }

    @Bean
    public Storage storage(final Credentials credentials,
                           final GoogleStorageProperties sp) {

        final var transportOptions = HttpTransportOptions.newBuilder()
                .setConnectTimeout(sp.getConnectTimeout())
                .setReadTimeout(sp.getReadTimeout())
                .build();

        final var retrySettings = RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(sp.getRetryDelay()))
                .setMaxRetryDelay(Duration.ofMillis(sp.getRetryMaxDelay()))
                .setMaxAttempts(sp.getRetryMaxAttempts())
                .setRetryDelayMultiplier(sp.getRetryMultiplier())
                .build();

        final var options = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setTransportOptions(transportOptions)
                .setRetrySettings(retrySettings)
                .build();

        return options.getService();
    }
}
