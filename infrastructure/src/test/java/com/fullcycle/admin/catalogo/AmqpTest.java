package com.fullcycle.admin.catalogo;

import com.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author kalil.peixoto
 * @date 3/16/23 22:16
 * @email kalilmvp@gmail.com
 */
@Target(TYPE)
@Retention(RUNTIME)
@Inherited
@ActiveProfiles(value = "test-integration")
@SpringBootTest(classes = WebServerConfig.class)
public @interface AmqpTest { }
