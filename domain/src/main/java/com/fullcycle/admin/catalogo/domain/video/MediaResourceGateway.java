package com.fullcycle.admin.catalogo.domain.video;

/**
 * @author kalil.peixoto
 * @date 4/30/24 23:21
 * @email kalilmvp@gmail.com
 */
public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);
    ImageMedia storeImage(VideoID anId, VideoResource aResource);
    void clearResources(VideoID anId);
}
