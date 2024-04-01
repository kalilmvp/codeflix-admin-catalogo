package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record CreateCastMemberOutput(String id) {

    public static CreateCastMemberOutput from(final CastMemberID castMemberID) {
        return new CreateCastMemberOutput(castMemberID.getValue());
    }

    public static CreateCastMemberOutput from(final CastMember aCastMember) {
        return new CreateCastMemberOutput(aCastMember.getId().getValue());
    }
}
