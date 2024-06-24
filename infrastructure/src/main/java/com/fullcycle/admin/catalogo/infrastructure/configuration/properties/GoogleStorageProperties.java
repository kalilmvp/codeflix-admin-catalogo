package com.fullcycle.admin.catalogo.infrastructure.configuration.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author kalil.peixoto
 * @date 6/24/24 20:59
 * @email kalilmvp@gmail.com
 */
public class GoogleStorageProperties implements InitializingBean {
    private final Logger LOGGER = LoggerFactory.getLogger(GoogleStorageProperties.class);

    private String bucket;
    private int connectTimeout;
    private int readTimeout;
    private int retryDelay;
    private int retryMaxAttempts;
    private int retryMaxDelay;
    private double retryMultiplier;

    public String getBucket() {
        return bucket;
    }

    public GoogleStorageProperties setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public GoogleStorageProperties setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public GoogleStorageProperties setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public GoogleStorageProperties setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
        return this;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public GoogleStorageProperties setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
        return this;
    }

    public int getRetryMaxDelay() {
        return retryMaxDelay;
    }

    public GoogleStorageProperties setRetryMaxDelay(int retryMaxDelay) {
        this.retryMaxDelay = retryMaxDelay;
        return this;
    }

    public double getRetryMultiplier() {
        return retryMultiplier;
    }

    public GoogleStorageProperties setRetryMultiplier(double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
        return this;
    }

    @Override
    public String toString() {
        return "GoogleStorageProperties{" +
                "bucket='" + bucket + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", retryDelay=" + retryDelay +
                ", retryMaxAttempts=" + retryMaxAttempts +
                ", retryMaxDelay=" + retryMaxDelay +
                ", retryMultiplier=" + retryMultiplier +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.LOGGER.debug(this.toString());
    }
}
