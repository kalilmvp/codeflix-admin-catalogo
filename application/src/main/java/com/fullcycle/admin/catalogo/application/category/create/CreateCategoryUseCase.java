package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.application.UseCase;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import io.vavr.control.Either;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:55
 * @email kalilmvp@gmail.com
 */
public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
