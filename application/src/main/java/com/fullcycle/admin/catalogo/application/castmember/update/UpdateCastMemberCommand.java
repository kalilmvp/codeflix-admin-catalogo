package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record UpdateCastMemberCommand(String id, String name, CastMemberType type) {

    public static UpdateCastMemberCommand with(final String anId, final String aName, final CastMemberType aType) {
        return new UpdateCastMemberCommand(anId, aName, aType);
    }
}
