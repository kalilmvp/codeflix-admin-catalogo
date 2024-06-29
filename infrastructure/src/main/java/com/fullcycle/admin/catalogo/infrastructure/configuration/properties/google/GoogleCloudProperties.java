package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author kalil.peixoto
 * @date 6/24/24 20:57
 * @email kalilmvp@gmail.com
 */
public class GoogleCloudProperties implements InitializingBean {
    private final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudProperties.class);
    private String credentials;
    private String projectId;

    public String getCredentials() {
        return credentials;
    }

    public GoogleCloudProperties setCredentials(String credentials) {
        this.credentials = credentials;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public GoogleCloudProperties setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    @Override
    public String toString() {
        return "GoogleCloudProperties{" +
                ", projectId='" + projectId + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.LOGGER.debug(this.toString());
    }
}
