package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.category.Category;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record UpdateCastMemberOutput(String id) {

    public static UpdateCastMemberOutput from(final String anId) {
        return new UpdateCastMemberOutput(anId);
    }

    public static UpdateCastMemberOutput from(final CastMember aCastMember) {
        return new UpdateCastMemberOutput(aCastMember.getId().getValue());
    }
}
