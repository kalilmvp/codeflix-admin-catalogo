package com.fullcycle.admin.catalogo.domain.video;

/**
 * @author kalil.peixoto
 * @date 4/30/24 23:21
 * @email kalilmvp@gmail.com
 */
public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID anId, Resource aResource);
    ImageMedia storeImage(VideoID anId, Resource aResource);
    void clearResources(VideoID anId);
}
