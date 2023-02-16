package com.fullcycle.admin.catalogo.domain;

/**
 * @author kalil.peixoto
 * @date 2/15/23 14:32
 * @email kalilmvp@gmail.com
 */
public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        super(id);
    }
}
