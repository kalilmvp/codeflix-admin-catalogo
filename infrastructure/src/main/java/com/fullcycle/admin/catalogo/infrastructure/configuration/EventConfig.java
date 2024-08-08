package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fullcycle.admin.catalogo.infrastructure.services.EventService;
import com.fullcycle.admin.catalogo.infrastructure.services.impl.RabbitEventService;
import com.fullcycle.admin.catalogo.infrastructure.services.local.InMemoryEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author kalil.peixoto
 * @date 7/6/24 16:54
 * @email kalilmvp@gmail.com
 */
@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    @Profile({"development"})
    EventService videoCreatedInMemoryEventService() {
        return new InMemoryEventService();
    }

    @Bean
    @VideoCreatedQueue
    @ConditionalOnMissingBean
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties queueProperties,
            final RabbitOperations rabbitOperations
    ) {
        return new RabbitEventService(queueProperties.getExchange(),
                queueProperties.getRoutingKey(),
                rabbitOperations);
    }
}
