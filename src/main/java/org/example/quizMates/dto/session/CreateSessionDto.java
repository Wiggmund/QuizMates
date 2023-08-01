package org.example.quizMates.dto.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class CreateSessionDto {
    private String title;
    private String description;
    private LocalDateTime date;
    private Long bestStudent;
    private Long bestGroup;
    private Boolean status;
}
