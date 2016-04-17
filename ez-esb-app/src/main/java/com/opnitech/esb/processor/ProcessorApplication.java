package com.opnitech.esb.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
@EnableAutoConfiguration(exclude =
    {
        VelocityAutoConfiguration.class
    })
@ComponentScan
@EnableJpaRepositories(basePackages = "com.opnitech.esb.processor.persistence.jpa")
@EnableElasticsearchRepositories(basePackages = "com.opnitech.esb.processor.persistence.elastic")
@Profile("default")
public class ProcessorApplication extends SpringBootServletInitializer {

    private static Class<ProcessorApplication> APPLICATION_CLASS = ProcessorApplication.class;

    public static void main(String[] args) {

        SpringApplication.run(ProcessorApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(APPLICATION_CLASS);
    }
}
