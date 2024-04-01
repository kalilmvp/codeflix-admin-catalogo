package com.fullcycle.admin.catalogo.application.castmember.delete;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;

import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:56
 * @email kalilmvp@gmail.com
 */
public non-sealed class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultDeleteCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(String anIn) {
        this.castMemberGateway.deleteById(CastMemberID.from(anIn));
    }
}
