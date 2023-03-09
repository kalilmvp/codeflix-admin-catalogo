package com.fullcycle.admin.catalogo.application.category.create;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record CreateCategoryCommand(String name, String description, boolean isActive) {

    public static CreateCategoryCommand with(final String aName, final String aDescription, final boolean isActive) {
        return new CreateCategoryCommand(aName, aDescription, isActive);
    }
}
