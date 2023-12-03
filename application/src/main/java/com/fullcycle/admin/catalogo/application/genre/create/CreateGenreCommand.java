package com.fullcycle.admin.catalogo.application.genre.create;

import java.util.List;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:24
 * @email kalilmvp@gmail.com
 */
public record CreateGenreCommand(String name, boolean isActive, List<String> categories) {

    public static CreateGenreCommand with(String aName, Boolean isActive, List<String> categories) {
        return new CreateGenreCommand(aName, isActive != null ? isActive : true, categories);
    }
}
