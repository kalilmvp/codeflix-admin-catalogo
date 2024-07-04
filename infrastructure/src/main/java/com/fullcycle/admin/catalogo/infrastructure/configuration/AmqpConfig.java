package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kalil.peixoto
 * @date 7/4/24 17:18
 * @email kalilmvp@gmail.com
 */
@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queue.video-created")
    @VideoCreatedQueue
    public QueueProperties videoCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queue.video-encoded")
    @VideoEncodedQueue
    public QueueProperties videoEncodedQueueProperties() {
        return new QueueProperties();
    }
}
