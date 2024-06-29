package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author kalil.peixoto
 * @date 6/27/24 17:50
 * @email kalilmvp@gmail.com
 */
public class StorageProperties implements InitializingBean {
    private final Logger LOGGER = LoggerFactory.getLogger(StorageProperties.class);

    private String fileNamePattern;
    private String locationPattern;

    public String fileNamePattern() {
        return fileNamePattern;
    }

    public StorageProperties setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
        return this;
    }

    public String locationPattern() {
        return locationPattern;
    }

    public StorageProperties setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
        return this;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug(this.toString());
    }

    @Override
    public String toString() {
        return "StorageProperties{" +
                "fileNamePattern='" + fileNamePattern + '\'' +
                ", locationPattern='" + locationPattern + '\'' +
                '}';
    }
}
