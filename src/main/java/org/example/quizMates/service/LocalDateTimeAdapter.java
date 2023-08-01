package org.example.quizMates.service;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        try {
            return LocalDateTime.parse(jsonElement.getAsString(), formatter);
        } catch (DateTimeParseException e) {
            throw new JsonParseException("Invalid date format. Expected ISO_DATE_TIME format (yyyy-MM-dd'T'HH:mm:ss.SSS).", e);
        }
    }
}

