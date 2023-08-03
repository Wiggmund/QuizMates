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
public class SessionRecord {
    private Long session_id;
    private Long pair_id;
    private Long student_id;
    private Long host_id;
    private Double score;
    private String host_notes;
    private Boolean was_present;

}
