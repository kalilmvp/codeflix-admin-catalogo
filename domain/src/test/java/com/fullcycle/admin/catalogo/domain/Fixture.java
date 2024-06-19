package com.fullcycle.admin.catalogo.domain;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Resource;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.github.javafaker.Faker;

import java.time.Year;
import java.util.Set;

import static io.vavr.API.*;

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

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na Prática",
                "Não cometa esses erros ao trabalhar com microserviços",
                "Testes de Mutação, você não testa seu software correctamente"
        );
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static Video video() {
        return Video.newVideo(title(),
                Videos.description(),
                Year.of(year()),
                duration(),
                Videos.rating(),
                bool(),
                bool(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.wesley().getId(), CastMembers.kalil().getId()));
    }

    public static class Categories {
        private static final Category AULAS = Category.newCategory("Aulas", "Sem descrição", true);

        public static Category aulas() {
            return Category.with(AULAS);
        }
    }

    public static class Genres {
        private static final Genre TECH = Genre.newGenre("Technology", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }
    }

    public static class CastMembers {
        private static final CastMember WESLEY = CastMember.newCastMember("Wesley Safadão", CastMemberType.ACTOR);
        private static final CastMember KALIL = CastMember.newCastMember("Kalil Valares", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember kalil() {
            return CastMember.with(KALIL);
        }
    }

    public static class Videos {
        private static final Video SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na Prática",
                "System Design no Mercado Livre na Prática Description",
                Year.of(2022),
                duration(),
                rating(),
                bool(),
                bool(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.wesley().getId(), CastMembers.kalil().getId()));

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static String description() {
            return FAKER.options().option(
                    """
                                A skilled thief, who steals secrets from deep within the subconscious during the dream state, 
                                is given the task to plant an idea into the mind of a CEO. However, his tragic past may doom the project and 
                                his team to disaster.
                            """,
                    """
                                The film recounts the adventures of Gustave H, a legendary concierge at a famous European hotel between the wars, and Zero Moustafa, 
                                the lobby boy who becomes his most trusted friend. The story involves the theft of a priceless painting and the battle for an enormous
                                 family fortune.
                            """,
                    """
                                An unemployed South Korean family becomes entangled in a strange and unsettling reality when they start
                                 to provide services to a wealthy family, leading to a series of unexpected incidents that unravel their lives.
                            """
            );
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final byte[] content = "Contents".getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }
    }


}
