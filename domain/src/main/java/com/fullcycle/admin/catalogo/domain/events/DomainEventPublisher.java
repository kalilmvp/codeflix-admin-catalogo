package com.fullcycle.admin.catalogo.domain.events;

/**
 * @author kalil.peixoto
 * @date 7/5/24 17:34
 * @email kalilmvp@gmail.com
 */
@FunctionalInterface
public interface DomainEventPublisher {
    void publishEvent(DomainEvent event);
}
