package com.opnitech.esb.processor.factories;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.services.configuration.PropertyPlaceholder;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class PropertyPlaceholderFactory {

    public PropertyPlaceholderFactory() {
        // Default constructor
    }

    @Bean
    public PropertyPlaceholder getPropertyPlaceholder() {

        return new PropertyPlaceholder();
    }
}
