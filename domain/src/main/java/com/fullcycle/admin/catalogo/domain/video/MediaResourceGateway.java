package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.resource.Resource;

import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 4/30/24 23:21
 * @email kalilmvp@gmail.com
 */
public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);
    ImageMedia storeImage(VideoID anId, VideoResource aResource);
    Optional<Resource> getResource(VideoID anId, VideoMediaType aType);
    void clearResources(VideoID anId);
}
