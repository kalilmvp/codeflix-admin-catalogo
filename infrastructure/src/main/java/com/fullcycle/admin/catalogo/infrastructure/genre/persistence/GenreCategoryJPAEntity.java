package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import com.fullcycle.admin.catalogo.domain.category.CategoryID;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 1/13/24 23:35
 * @email kalilmvp@gmail.com
 */
@Entity
@Table(name = "genres_categories")
public class GenreCategoryJPAEntity {
    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJPAEntity genre;

    public GenreCategoryJPAEntity(){}

    private GenreCategoryJPAEntity(final GenreJPAEntity aGenre, final CategoryID aCategoryID) {
        this.id = GenreCategoryID.from(aGenre.getId(), aCategoryID.getValue());
        this.genre = aGenre;
    }

    public static GenreCategoryJPAEntity from(final GenreJPAEntity aGenre, final CategoryID aCategoryID) {
        return new GenreCategoryJPAEntity(aGenre, aCategoryID);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryJPAEntity that = (GenreCategoryJPAEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public GenreCategoryID getId() {
        return id;
    }

    public void setId(GenreCategoryID id) {
        this.id = id;
    }

    public GenreJPAEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreJPAEntity genre) {
        this.genre = genre;
    }
}
