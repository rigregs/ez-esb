package com.opnitech.esb.processor.factories.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import com.opnitech.esb.processor.configuration.PropertyPlaceholder;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class PropertyPlaceholderFactory {

    private static final String YML_CONFI_FILE_PARAMETER = "yml.confi.file";

    public PropertyPlaceholderFactory() {
        // Default constructor
    }

    @Bean
    public PropertyPlaceholder getPropertyPlaceholder() throws FileNotFoundException, IOException {

        String ympConfigFile = System.getProperty(YML_CONFI_FILE_PARAMETER);

        PropertyPlaceholder propertyPlaceholder = null;

        try (InputStreamReader reader = new FileReader(new File(ympConfigFile))) {
            Yaml yaml = new Yaml();

            propertyPlaceholder = yaml.loadAs(reader, PropertyPlaceholder.class);
        }

        return propertyPlaceholder;
    }
}
