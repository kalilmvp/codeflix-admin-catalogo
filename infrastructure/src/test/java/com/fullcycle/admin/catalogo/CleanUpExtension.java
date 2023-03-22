package com.fullcycle.admin.catalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

/**
 * @author kalil.peixoto
 * @date 3/19/23 11:00
 * @email kalilmvp@gmail.com
 */
public class CleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final var repositores = SpringExtension
                .getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values();

        cleanUp(repositores);
    }

    public void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
