package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record CreateCastMemberOutput(String id) {

    public static CreateCastMemberOutput from(final String anId) {
        return new CreateCastMemberOutput(anId);
    }

    public static CreateCastMemberOutput from(final CastMember aCastMember) {
        return new CreateCastMemberOutput(aCastMember.getId().getValue());
    }
}
