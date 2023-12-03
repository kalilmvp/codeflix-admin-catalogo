package com.fullcycle.admin.catalogo.application;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.reset;

/**
 * @author kalil.peixoto
 * @date 12/3/23 16:05
 * @email kalilmvp@gmail.com
 */
@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        reset(this.getMocks().toArray());
    }

    protected abstract List<Object> getMocks();
}
