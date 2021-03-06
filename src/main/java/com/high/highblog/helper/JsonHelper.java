package com.high.highblog.helper;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public final class JsonHelper {

    private static final ObjectMapper MAPPER = createObjectMapper();

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
        APPLICATION_JSON.getType(),
        APPLICATION_JSON.getSubtype(), UTF_8);

    public static <T> T readJSONFile(final File jsonFile, final Class<T> clazz)
        throws IOException {

        return MAPPER.readValue(jsonFile, clazz);
    }

    public static <T> T readString(final String content, final Class<T> clazz)
        throws IOException {

        return MAPPER.readValue(content, clazz);
    }

    public static byte[] convertObjectToJsonBytes(final Object object)
        throws IOException {

        return MAPPER.writeValueAsBytes(object);
    }

    public static String convertObjectToString(final Object object)
        throws IOException {

        return MAPPER.writeValueAsString(object);
    }

    public static <T> T convertValue(final Object fromValue, final Class<T> toValueType) {
        return MAPPER.convertValue(fromValue, toValueType);
    }

    private JsonHelper() {
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        return mapper;
    }
}
