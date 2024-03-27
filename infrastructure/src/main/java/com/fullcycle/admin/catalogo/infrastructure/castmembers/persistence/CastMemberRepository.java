package com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kalil.peixoto
 * @date 3/15/23 22:13
 * @email kalilmvp@gmail.com
 */
public interface CastMemberRepository extends JpaRepository<CastMembersJPAEntity, String> {

    Page<CastMembersJPAEntity> findAll(Specification<CastMembersJPAEntity> whereClause, Pageable page);
}
