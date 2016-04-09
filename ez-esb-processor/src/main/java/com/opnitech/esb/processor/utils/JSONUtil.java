package com.opnitech.esb.processor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public final class JSONUtil {

    private JSONUtil() {

        // Default constructor
    }

    public static <T> T clone(T value) {

        if (value == null) {
            return null;
        }

        String marshall = marshall(value);

        @SuppressWarnings("unchecked")
        T unmarshall = (T) unmarshall(value.getClass(), marshall);

        return unmarshall;
    }

    public static String marshall(Object value) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

            return objectMapper.writeValueAsString(value);
        }
        catch (Exception e) {
            throw new RuntimeException("could not map EmailRetryMessage to json object", e);
        }
    }

    public static <T> T unmarshall(Class<T> clazz, String content) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

            T value = objectMapper.readValue(content, clazz);

            return value;
        }
        catch (Exception e) {
            throw new RuntimeException("could not map EmailRetryMessage to json object", e);
        }
    }
}
