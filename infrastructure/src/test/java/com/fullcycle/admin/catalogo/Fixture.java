package com.fullcycle.admin.catalogo;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.github.javafaker.Faker;

/**
 * @author kalil.peixoto
 * @date 3/26/24 09:47
 * @email kalilmvp@gmail.com
 */
public final class Fixture {
    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static class CastMember {
        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }
    }
}
