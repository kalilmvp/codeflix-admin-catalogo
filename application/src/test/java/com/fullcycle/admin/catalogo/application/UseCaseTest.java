package com.fullcycle.admin.catalogo.application;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.reset;

/**
 * @author kalil.peixoto
 * @date 12/3/23 16:05
 * @email kalilmvp@gmail.com
 */
@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        reset(this.getMocks().toArray());
    }

    protected abstract List<Object> getMocks();

    protected List<String> asString(final List<? extends Identifier> categories) {
        return categories.stream()
                .map(Identifier::getValue)
                .toList();
    }

    protected Set<String> asString(final Set<? extends Identifier> values) {
        return values.stream()
                .map(Identifier::getValue)
                .collect(Collectors.toSet());
    }
}
