package org.example.quizMates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.quizMates.utils.LocalDateTimeAdapter;

import java.time.LocalDateTime;

public class ApplicationConfig {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();
}
