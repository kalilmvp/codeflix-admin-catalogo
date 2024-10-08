package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.ValueObject;
import com.fullcycle.admin.catalogo.domain.resource.Resource;

import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 6/25/24 17:06
 * @email kalilmvp@gmail.com
 */
public class VideoResource extends ValueObject {
    private final Resource resource;
    private final VideoMediaType type;

    public VideoResource(Resource resource, VideoMediaType type) {
        this.resource = Objects.requireNonNull(resource);
        this.type = Objects.requireNonNull(type);
    }

    public static VideoResource with(final Resource aResource, final VideoMediaType aType) {
        return new VideoResource(aResource, aType);
    }

    public Resource resource() {
        return resource;
    }

    public VideoMediaType type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoResource that = (VideoResource) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }
}
