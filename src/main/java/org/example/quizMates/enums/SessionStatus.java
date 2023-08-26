package org.example.quizMates.enums;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public enum SessionStatus {
    @SerializedName("CREATED")
    CREATED("CREATED"),

    @SerializedName("STARTED")
    STARTED("STARTED"),

    @SerializedName("FINISHED")
    FINISHED("FINISHED");

    private final String name;

    SessionStatus(String name) {
        this.name = name;
    }
}
