package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * @author kalil.peixoto
 * @date 1/14/24 21:57
 * @email kalilmvp@gmail.com
 */
public interface VideoRepository extends JpaRepository<VideoJPAEntity, String> {

    @Query("""
            select new com.fullcycle.admin.catalogo.domain.video.VideoPreview(
                v.id as id,
                v.title as title,
                v.description as description,
                v.createdAt as createdAt,
                v.updatedAt as updatedAt
            )
            from Video v
                join v.categories c 
                join v.genres g
                join v.castMembers cm
            where
                (:terms is null or UPPER(v.title) like :terms )
            and
                (:categories is null or c.id.categoryId in :categories )
            and
                (:genres is null or g.id.genreId in :genres )
            and
                (:castMembers is null or cm.id.castMemberId in :castMembers )
           """)
    Page<VideoPreview> findAll(@Param("terms") String terms,
                               @Param("categories") Set<String> categories,
                               @Param("genres") Set<String> genres,
                               @Param("castMembers") Set<String> castMembers,
                               Pageable page);

}
