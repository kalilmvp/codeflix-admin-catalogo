package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.application.UseCase;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:55
 * @email kalilmvp@gmail.com
 */
public sealed abstract class CreateCastMemberUseCase extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase { }
