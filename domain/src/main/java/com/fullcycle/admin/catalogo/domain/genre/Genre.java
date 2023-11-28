package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 2/15/23 14:07
 * @email kalilmvp@gmail.com
 */
public class Genre extends AggregateRoot<GenreID> implements Cloneable {

    private String name;
    private boolean active;
    private List<Category> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Genre(final GenreID anId,
                  final String aName,
                  final boolean isActive,
                  final List<Category> aCategories,
                  final Instant aCreatedAt,
                  final Instant aUpdatedAt,
                  final Instant aDeletedAt) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = aCategories;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' should not be null");;
        this.deletedAt = aDeletedAt;

        final var notification = Notification.create();
        validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Failed to validate aggregate Genre", notification);
        }
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(GenreID.unique(), aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    private static Genre with(final GenreID anId,
                              final String aName,
                              final boolean isActive,
                              final List<Category> aCategories,
                              final Instant aCreatedAt,
                              final Instant aUpdatedAt,
                              final Instant aDeletedAt) {
        return new Genre(anId, aName, isActive, aCategories, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    private static Genre with(final Genre aGenre) {
        return new Genre(aGenre.getId(), aGenre.getName(), aGenre.isActive(), aGenre.getCategories()
                        ,aGenre.getCreatedAt(), aGenre.getUpdatedAt(), aGenre.getDeletedAt());
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public Genre deactivate() {
        if (this.getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }

        this.active = Boolean.FALSE;
        this.updatedAt = Instant.now();
        return this;
    }


    public Genre activate() {
        this.deletedAt = null;
        this.active = Boolean.TRUE;
        this.updatedAt = Instant.now();

        return this;
    }


    public Genre update(final String aName, final String aDescription, final Boolean aIsActive) {
        if (aIsActive) {
            this.activate();
        } else {
            this.deactivate();
        }

        this.name = aName;
        this.updatedAt = Instant.now();

        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public Genre clone() {
        try {
            return (Genre) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
