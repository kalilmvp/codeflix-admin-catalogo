package com.fullcycle.admin.catalogo.application.category.update;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record UpdateCategoryCommand(String id, String name, String description, boolean isActive) {

    public static UpdateCategoryCommand with(final String anId, final String aName, final String aDescription, final boolean isActive) {
        return new UpdateCategoryCommand(anId, aName, aDescription, isActive);
    }
}
