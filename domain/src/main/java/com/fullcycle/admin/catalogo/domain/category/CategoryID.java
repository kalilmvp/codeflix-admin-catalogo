package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * @author kalil.peixoto
 * @date 2/16/23 10:11
 * @email kalilmvp@gmail.com
 */
public class CategoryID extends Identifier  {
    private final String value;

    private CategoryID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CategoryID unique() {
        return CategoryID.from(IdUtils.uuid());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
