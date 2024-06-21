package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author kalil.peixoto
 * @date 1/14/24 21:57
 * @email kalilmvp@gmail.com
 */
public interface GenreRepository extends JpaRepository<GenreJPAEntity, String> {

    Page<GenreJPAEntity> findAll(Specification<GenreJPAEntity> whereClause, Pageable page);

    @Query(value = "SELECT g.id FROM Genre g WHERE g.id IN :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}
