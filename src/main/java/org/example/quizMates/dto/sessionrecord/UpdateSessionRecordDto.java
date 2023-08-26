package org.example.quizMates.dto.sessionrecord;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.quizMates.enums.SessionRecordAction;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class UpdateSessionRecordDto {
    private Long id;
    private Long sessionId;
    private Long pairId;
    private Long studentId;
    private Long hostId;
    private Double score;
    private String hostNotes;
    private Boolean wasPresent;
    private SessionRecordAction action;
    private String question;
}
