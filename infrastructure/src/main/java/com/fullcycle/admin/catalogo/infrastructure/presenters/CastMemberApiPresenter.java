package com.fullcycle.admin.catalogo.infrastructure.presenters;

import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.CastMembersListOutput;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CastMemberListResponse;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CastMemberResponse;

/**
 * @author kalil.peixoto
 * @date 16/03/202 21:18
 * @email kalilmvp@gmail.com
 */
public interface CastMemberApiPresenter {

    static CastMemberResponse present(final CastMemberOutput output) {
        return new CastMemberResponse(
                output.id(),
                output.name(),
                output.type().name(),
                output.createdAt().toString(),
                output.updatedAt().toString());
    }

    static CastMemberListResponse present(final CastMembersListOutput output) {
        return new CastMemberListResponse(
                output.id(),
                output.name(),
                output.type().name(),
                output.createdAt().toString());
    }
}
