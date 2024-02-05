package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kalil.peixoto
 * @date 1/14/24 21:57
 * @email kalilmvp@gmail.com
 */
public interface GenreRepository extends JpaRepository<GenreJPAEntity, String> {

  Page<GenreJPAEntity> findAll(Specification<GenreJPAEntity> whereClause, Pageable page);
}
