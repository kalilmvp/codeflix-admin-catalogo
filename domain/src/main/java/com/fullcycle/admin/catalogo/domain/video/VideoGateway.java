package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 4/26/24 11:26
 * @email kalilmvp@gmail.com
 */
public interface VideoGateway {

    Video create(Video aVideo);
    Video update(Video aVideo);
    void deleteById(VideoID anId);
    Optional<Video> findById(VideoID anId);
    Pagination<Video> findAll(VideoSearchQuery aQuery);
}
