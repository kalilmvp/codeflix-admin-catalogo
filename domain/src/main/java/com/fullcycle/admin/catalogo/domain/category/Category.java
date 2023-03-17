package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;

/**
 * @author kalil.peixoto
 * @date 2/15/23 14:07
 * @email kalilmvp@gmail.com
 */
public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    private Category(final CategoryID id,
                     final String name,
                     final String description,
                     final boolean active,
                     final Instant createdAt,
                     final Instant updatedAt,
                     final Instant deletedAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    private Instant deletedAt;


    public static Category newCategory(final String name, final String description, final boolean isActive) {
        final Instant now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(CategoryID.unique(), name, description, isActive, now, now, deletedAt);
    }

    public static Category with(final CategoryID anId, final String name, final String description, final boolean active,
                                final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.name,
                aCategory.description,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        if (this.getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }

        this.active = Boolean.FALSE;
        this.updatedAt = Instant.now();
        return this;
    }


    public Category activate() {
        this.deletedAt = null;
        this.active = Boolean.TRUE;
        this.updatedAt = Instant.now();

        return this;
    }


    public Category update(final String aName, final String aDescription, final Boolean aIsActive) {
        if (aIsActive) {
            this.activate();
        } else {
            this.deactivate();
        }

        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();

        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
