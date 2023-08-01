package org.example.quizMates.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class Session {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private Long best_student;
    private Long best_group;
    private Boolean status;
}
