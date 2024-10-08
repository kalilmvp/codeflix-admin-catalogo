package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.*;

/**
 * @author kalil.peixoto
 * @date 1/14/24 21:32
 * @email kalilmvp@gmail.com
 */
@Entity(name = "Genre")
@Table(name = "genres")
public class GenreJPAEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "active", nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = EAGER, orphanRemoval = true)
    private Set<GenreCategoryJPAEntity> categories;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public GenreJPAEntity() {
    }

    private GenreJPAEntity(final String anId,
                           final String aName,
                           final boolean isActive,
                           final Instant createdAt,
                           final Instant updatedAt,
                           final Instant deletedAt) {

        this.id = anId;
        this.name = aName;
        this.active = isActive;
        this.categories = new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static GenreJPAEntity from(final Genre aGenre) {
        final var entity = new GenreJPAEntity(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt());

        aGenre.getCategories().forEach(entity::addCategory);

        return entity;
    }

    public Genre toAggregate() {
        return Genre.with(
                GenreID.from(this.getId()),
                this.getName(),
                this.isActive(),
                this.getCategoryIDs(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt()
        );
    }

    private void addCategory(final CategoryID anId) {
        this.categories.add(GenreCategoryJPAEntity.from(this, anId));
    }

    private void removeCategory(final CategoryID anId) {
        this.categories.remove(GenreCategoryJPAEntity.from(this, anId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<CategoryID> getCategoryIDs() {
        return this.getCategories().stream()
                .map(it -> CategoryID.from(it.getId().getCategoryId()))
                .toList();
    }

    public Set<GenreCategoryJPAEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<GenreCategoryJPAEntity> categories) {
        this.categories = categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
