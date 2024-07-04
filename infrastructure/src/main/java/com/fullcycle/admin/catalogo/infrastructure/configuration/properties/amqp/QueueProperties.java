package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author kalil.peixoto
 * @date 7/4/24 17:15
 * @email kalilmvp@gmail.com
 */
public class QueueProperties implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueProperties.class);
    private String exchange;
    private String routingKey;
    private String queue;

    @Override
    public String toString() {
        return "QueueProperties{" +
                "exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", queue='" + queue + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug(this.toString());
    }

    public String getExchange() {
        return exchange;
    }

    public QueueProperties setExchange(String exchange) {
        this.exchange = exchange;
        return this;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public QueueProperties setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public String getQueue() {
        return queue;
    }

    public QueueProperties setQueue(String queue) {
        this.queue = queue;
        return this;
    }
}
