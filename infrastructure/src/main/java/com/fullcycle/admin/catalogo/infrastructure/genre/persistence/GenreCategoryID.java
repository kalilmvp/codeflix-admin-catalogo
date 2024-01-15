package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 1/13/24 23:35
 * @email kalilmvp@gmail.com
 */
@Embeddable
public class GenreCategoryID implements Serializable {

    @Column(name = "genre_id", nullable = false)
    private String genreId;
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    public GenreCategoryID(){}

    private GenreCategoryID(final String aGenreId, final String aCategoryId) {
        this.genreId = aGenreId;
        this.categoryId = aCategoryId;
    }

    public static GenreCategoryID from(final String aGenreId, final String aCategoryId) {
        return new GenreCategoryID(aGenreId, aCategoryId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(getGenreId(), that.getGenreId()) && Objects.equals(getCategoryId(), that.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGenreId(), getCategoryId());
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
