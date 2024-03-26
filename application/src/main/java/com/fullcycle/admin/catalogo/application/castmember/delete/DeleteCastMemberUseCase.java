package com.fullcycle.admin.catalogo.application.castmember.delete;

import com.fullcycle.admin.catalogo.application.UnitUseCase;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:55
 * @email kalilmvp@gmail.com
 */
public sealed abstract class DeleteCastMemberUseCase extends UnitUseCase<String> permits DefaultDeleteCastMemberUseCase {

}
