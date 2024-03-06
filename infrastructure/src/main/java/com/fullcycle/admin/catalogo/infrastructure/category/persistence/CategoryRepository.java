package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author kalil.peixoto
 * @date 3/15/23 22:13
 * @email kalilmvp@gmail.com
 */
public interface CategoryRepository extends JpaRepository<CategoryJPAEntity, String> {

    Page<CategoryJPAEntity> findAll(Specification<CategoryJPAEntity> whereClause, Pageable page);

    @Query(value = "SELECT c.id FROM Category c WHERE c.id IN :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}
