package com.fullcycle.admin.catalogo.domain.resource;

import com.fullcycle.admin.catalogo.domain.ValueObject;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 4/30/24 11:26
 * @email kalilmvp@gmail.com
 */
public class Resource extends ValueObject {

    private String checksum;
    private final byte[] content;
    private final String contentType;
    private final String name;

    private Resource(final String checksum, byte[] content, String contentType, String name) {
        this.checksum = Objects.requireNonNull(checksum);
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
    }

    public static Resource with(final String checksum,
                                final byte[] content,
                                final String contentType,
                                final String name) {
        return new Resource(checksum, content, contentType, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(checksum, resource.checksum)
                && Objects.deepEquals(content, resource.content)
                && Objects.equals(contentType, resource.contentType)
                && Objects.equals(name, resource.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, Arrays.hashCode(content), contentType, name);
    }

    public String checksum() {
        return checksum;
    }

    public byte[] content() {
        return content;
    }

    public String contentType() {
        return contentType;
    }

    public String name() {
        return name;
    }
}
