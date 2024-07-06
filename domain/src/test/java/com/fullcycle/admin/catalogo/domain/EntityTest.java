package com.fullcycle.admin.catalogo.domain;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 7/5/24 23:14
 * @email kalilmvp@gmail.com
 */
public class EntityTest {

    @Test
    public void givenNullAsEvents_whenInstantiate_shouldBeOk() {
        // given
        final List<DomainEvent> events = null;

        // when
        final var aEntity = new DummyEntity(new DummyID(), events);

        // then
        assertNotNull(aEntity.getDomainEvents());
        assertTrue(aEntity.getDomainEvents().isEmpty());
    }

    @Test
    public void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {
        // given
        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());

        final var expectedDomainEventsSize = 1;

        // when
        final var aEntity = new DummyEntity(new DummyID(), events);

        // then
        assertNotNull(aEntity.getDomainEvents());
        assertEquals(expectedDomainEventsSize, aEntity.getDomainEvents().size());

        assertThrows(RuntimeException.class, () -> {
            final var actualEvents = aEntity.getDomainEvents();
            actualEvents.add(new DummyEvent());
        });
    }

    @Test
    public void givenDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {
        // given
        final var aEntity = new DummyEntity(new DummyID(), new ArrayList<>());
        final var expectedDomainEventsSize = 1;

        // when
        aEntity.registerEvent(new DummyEvent());

        // then
        assertNotNull(aEntity.getDomainEvents());
        assertEquals(expectedDomainEventsSize, aEntity.getDomainEvents().size());
    }

    @Test
    public void givenAFewDomainEvents_whenCallsPublishEvents_shouldCallPublisherAndClearTheList() {
        // given
        final var expectedDomainEventsSize = 0;
        final var expectedSentEvents = 2;
        final var aEntity = new DummyEntity(new DummyID(), new ArrayList<>());
        aEntity.registerEvent(new DummyEvent());
        aEntity.registerEvent(new DummyEvent());
        final var counter = new AtomicInteger(0);

        assertEquals(2, aEntity.getDomainEvents().size());

        // when
        aEntity.publishDomainEvent(event -> {
            counter.incrementAndGet();
        });

        // then
        assertEquals(expectedDomainEventsSize, aEntity.getDomainEvents().size());
        assertEquals(expectedSentEvents, counter.get());
    }

    public static class DummyEvent implements DomainEvent {

        @Override
        public Instant ocurredOn() {
            return InstantUtils.now();
        }
    }

    public static class DummyID extends Identifier {
        private final String id;

        public DummyID() {
            this.id = IdUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    public static class DummyEntity extends Entity<DummyID> {
        public DummyEntity(final DummyID dummyID,
                           final List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {

        }
    }

}
