package com.high.highblog.model.converter;

import com.high.highblog.helper.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
public class MapToJsonConverter
        implements AttributeConverter<Map<String, Object>, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapToJsonConverter.class);

    @Override
    public String convertToDatabaseColumn(final Map<String, Object> attribute) {
        try {
            return JsonHelper.convertMapToString(attribute);
        } catch (IOException e) {
            LOGGER.error("Could not convert map to json string.");
            return null;
        }
    }

    @Override
    @SuppressWarnings("uncheck")
    public Map<String, Object> convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return new HashMap<>();
        }
        try {
            return JsonHelper.readString(dbData, HashMap.class);
        } catch (IOException e) {
            LOGGER.error("Convert error while trying to convert string(JSON) to map data structure.");
        }
        return new HashMap<>();
    }
}
