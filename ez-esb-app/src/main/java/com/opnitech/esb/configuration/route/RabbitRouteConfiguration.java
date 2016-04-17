package com.opnitech.esb.configuration.route;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RabbitRouteConfiguration extends RouteConfiguration {

    private String exchangeName;
    private boolean autoDelete;
    private int threadPoolSize;
    private String routingKey;
    private String queue;

    private String vhost;
    private String host;
    private int port;
    private String username;
    private String password;

    public RabbitRouteConfiguration() {
        // Default constructor
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(this.host).append(this.port).append(this.username).append(this.password).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof RabbitRouteConfiguration)) {
            return false;
        }

        RabbitRouteConfiguration other = (RabbitRouteConfiguration) obj;

        return new EqualsBuilder().append(this.host, other.getHost()).append(this.port, other.getPort())
                .append(this.username, other.getUsername()).append(this.password, other.getPassword()).isEquals();
    }

    public String getHost() {

        return this.host;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public int getPort() {

        return this.port;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return this.password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getExchangeName() {

        return this.exchangeName;
    }

    public void setExchangeName(String exchangeName) {

        this.exchangeName = exchangeName;
    }

    public int getThreadPoolSize() {

        return this.threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {

        this.threadPoolSize = threadPoolSize;
    }

    public String getRoutingKey() {

        return this.routingKey;
    }

    public void setRoutingKey(String routingKey) {

        this.routingKey = routingKey;
    }

    public String getQueue() {

        return this.queue;
    }

    public void setQueue(String queue) {

        this.queue = queue;
    }

    public String getVhost() {

        return this.vhost;
    }

    public void setVhost(String vhost) {

        this.vhost = vhost;
    }

    public boolean isAutoDelete() {

        return this.autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {

        this.autoDelete = autoDelete;
    }
}
