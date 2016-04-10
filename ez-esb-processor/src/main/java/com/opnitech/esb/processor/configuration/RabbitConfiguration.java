package com.opnitech.esb.processor.configuration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RabbitConfiguration {

    private String host;
    private int port;
    private String username;
    private String password;

    public RabbitConfiguration() {
        // Default constructor
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
}
