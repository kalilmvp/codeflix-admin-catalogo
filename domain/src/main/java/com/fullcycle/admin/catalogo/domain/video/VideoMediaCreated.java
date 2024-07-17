package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;

import java.time.Instant;

/**
 * @author kalil.peixoto
 * @date 7/5/24 23:46
 * @email kalilmvp@gmail.com
 */
public record VideoMediaCreated(String resourceId,
                                String filePath,
                                Instant ocurredOn) implements DomainEvent {

    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, InstantUtils.now());
    }
}
