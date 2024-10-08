package com.fullcycle.admin.catalogo;

import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import io.vavr.collection.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author kalil.peixoto
 * @date 3/19/23 11:00
 * @email kalilmvp@gmail.com
 */
public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                appContext.getBean(VideoRepository.class),
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class),
                appContext.getBean(CastMemberRepository.class)
        ));
    }

    public void cleanUp(final List<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
