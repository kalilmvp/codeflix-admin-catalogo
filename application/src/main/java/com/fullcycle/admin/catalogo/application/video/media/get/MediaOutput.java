package com.fullcycle.admin.catalogo.application.video.media.get;

import com.fullcycle.admin.catalogo.domain.resource.Resource;

/**
 * @author kalil.peixoto
 * @date 7/1/24 11:21
 * @email kalilmvp@gmail.com
 */
public record MediaOutput(
        String name,
        String contentType,
        byte[] content
) {
    public static MediaOutput with(Resource aResource) {
        return new MediaOutput(aResource.name(), aResource.contentType(), aResource.content());
    }
}
