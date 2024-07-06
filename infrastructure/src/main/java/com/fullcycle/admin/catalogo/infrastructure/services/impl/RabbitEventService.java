package com.fullcycle.admin.catalogo.infrastructure.services.impl;

import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.services.EventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 7/6/24 16:46
 * @email kalilmvp@gmail.com
 */
public class RabbitEventService implements EventService {

    private final String exchange;
    private final String routingKey;
    private final RabbitOperations rabbitOperations;

    public RabbitEventService(final String exchange,
                              final String routingKey,
                              final RabbitOperations rabbitOperations) {
        this.exchange = Objects.requireNonNull(exchange);
        this.routingKey = Objects.requireNonNull(routingKey);
        this.rabbitOperations = Objects.requireNonNull(rabbitOperations);
    }

    @Override
    public void send(final Object event) {
        this.rabbitOperations.convertAndSend(this.exchange, this.routingKey, Json.writeValueAsString(event));
    }
}
