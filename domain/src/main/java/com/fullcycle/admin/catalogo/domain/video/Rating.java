package com.fullcycle.admin.catalogo.domain.video;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 4/24/24 21:46
 * @email kalilmvp@gmail.com
 */
public enum Rating {
    ER("18"),
    L("L"),
    AGE_10("10"),
    AGE_12("12"),
    AGE_14("14"),
    AGE_16("16"),
    AGE_18("18");

    private final String name;

    Rating(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<Rating> of(final String label) {
        return Arrays.stream(values())
                .filter(rating -> rating.getName().equalsIgnoreCase(label))
                .findFirst();
    }
}
