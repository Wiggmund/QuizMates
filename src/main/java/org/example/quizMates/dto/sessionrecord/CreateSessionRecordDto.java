package org.example.quizMates.dto.sessionrecord;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class CreateSessionRecordDto {
    private Long session_id
    private Double score;
    private String host_notes;
    private Boolean was_present;
}
