package com.fullcycle.admin.catalogo.domain.events;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author kalil.peixoto
 * @date 7/5/24 17:34
 * @email kalilmvp@gmail.com
 */
public interface DomainEvent extends Serializable {
    Instant ocurredOn();
}
