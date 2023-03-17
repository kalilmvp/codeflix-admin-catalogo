package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kalil.peixoto
 * @date 3/15/23 22:13
 * @email kalilmvp@gmail.com
 */
public interface CategoryRepository extends JpaRepository<CategoryJPAEntity, String> {
}
