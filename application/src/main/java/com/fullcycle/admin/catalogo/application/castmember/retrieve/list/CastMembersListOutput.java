package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

/**
 * @author kalil.peixoto
 * @date 3/12/23 08:28
 * @email kalilmvp@gmail.com
 */
public record CastMembersListOutput(String id,
                                    String name,
                                    CastMemberType type,
                                    Instant createdAt) {

    public static CastMembersListOutput from(CastMember aCastMember) {
        return new CastMembersListOutput(
                aCastMember.getId().getValue(),
                aCastMember.getName(),
                aCastMember.getType(),
                aCastMember.getCreatedAt());
    }
}
