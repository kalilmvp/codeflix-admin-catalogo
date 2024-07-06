package com.fullcycle.admin.catalogo.domain;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;

import java.util.Collections;
import java.util.List;

/**
 * @author kalil.peixoto
 * @date 2/15/23 14:32
 * @email kalilmvp@gmail.com
 */
public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }

    protected AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}
