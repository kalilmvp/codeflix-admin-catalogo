package com.fullcycle.admin.catalogo.infrastructure.services.local;

import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kalil.peixoto
 * @date 6/25/24 08:19
 * @email kalilmvp@gmail.com
 */
public class InMemoryEventService implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(Object event) {
        LOGGER.info("Event observed: " + Json.writeValueAsString(event));
    }
}
