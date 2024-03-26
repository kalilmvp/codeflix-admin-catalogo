package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:24
 * @email kalilmvp@gmail.com
 */
public record CreateCastMemberCommand(String name, CastMemberType type) {

    public static CreateCastMemberCommand with(String aName, CastMemberType aType) {
        return new CreateCastMemberCommand(aName, aType);
    }
}
