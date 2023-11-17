package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kalil.peixoto
 * @date 11/16/23 17:39
 * @email kalilmvp@gmail.com
 */
@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
}
