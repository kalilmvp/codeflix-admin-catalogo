package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import java.time.Instant;

/**
 * @author kalil.peixoto
 * @date 3/11/23 21:18
 * @email kalilmvp@gmail.com
 */
public record CastMemberOutput(String id,
                               String name,
                               CastMemberType type,
                               Instant createdAt,
                               Instant updatedAt) {

    public static CastMemberOutput from(final CastMember aCastMember) {
        return new CastMemberOutput(aCastMember.getId().getValue(),
                aCastMember.getName(),
                aCastMember.getType(),
                aCastMember.getCreatedAt(),
                aCastMember.getUpdatedAt());
    }
}
