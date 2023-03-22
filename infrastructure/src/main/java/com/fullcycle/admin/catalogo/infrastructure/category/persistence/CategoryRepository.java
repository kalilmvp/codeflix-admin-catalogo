package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kalil.peixoto
 * @date 3/15/23 22:13
 * @email kalilmvp@gmail.com
 */
public interface CategoryRepository extends JpaRepository<CategoryJPAEntity, String> {

    Page<CategoryJPAEntity> findAll(Specification<CategoryJPAEntity> whereClause, Pageable page);
}
