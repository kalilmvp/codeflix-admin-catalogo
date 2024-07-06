package com.fullcycle.admin.catalogo.domain;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.domain.events.DomainEventPublisher;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 2/15/23 14:24
 * @email kalilmvp@gmail.com
 */
public abstract class Entity<ID extends Identifier> {

    private final ID id;
    private final List<DomainEvent> domainEvents;

    protected Entity(final ID id, List<DomainEvent> domainEvents) {
        Objects.requireNonNull(id, "'id' must not be null");
        this.id = id;
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
    }

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    public void publishDomainEvent(final DomainEventPublisher publisher) {
        if (publisher == null) return;
        this.getDomainEvents().forEach(publisher::publishEvent);
        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent domainEvent) {
        if (domainEvent != null) {
            this.domainEvents.add(domainEvent);
        }
    }

    public abstract void validate(ValidationHandler handler);

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
