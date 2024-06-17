package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kalil.peixoto
 * @date 1/14/24 21:57
 * @email kalilmvp@gmail.com
 */
public interface VideoRepository extends JpaRepository<VideoJPAEntity, String> {

}
