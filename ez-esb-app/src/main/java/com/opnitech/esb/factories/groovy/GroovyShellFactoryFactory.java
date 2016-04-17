package com.opnitech.esb.factories.groovy;

import org.apache.camel.Exchange;
import org.apache.camel.language.groovy.GroovyShellFactory;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.opnitech.esb.client.util.JSONUtil;
import com.opnitech.esb.client.v1.model.notification.DocumentChangeNotification;

import groovy.lang.GroovyShell;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class GroovyShellFactoryFactory {

    public GroovyShellFactoryFactory() {
        // Default constructor
    }

    @Bean
    public GroovyShellFactory getGroovyShellFactory() {

        GroovyShellFactory groovyShellFactory = new GroovyShellFactory() {

            @Override
            public GroovyShell createGroovyShell(Exchange arg0) {

                ImportCustomizer importCustomizer = new ImportCustomizer();
                importCustomizer.addStaticStars(JSONUtil.class.getName());

                importCustomizer.addImports(DocumentChangeNotification.class.getName(), JsonInclude.class.getName(),
                        JsonIgnoreProperties.class.getName(), Include.class.getName(), JSONUtil.class.getName());

                CompilerConfiguration configuration = new CompilerConfiguration();
                configuration.addCompilationCustomizers(importCustomizer);
                return new GroovyShell(configuration);
            }
        };

        return groovyShellFactory;
    }
}
