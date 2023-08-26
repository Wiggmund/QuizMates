package org.example.quizMates.dto.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.quizMates.enums.SessionStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class UpdateSessionDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private Long bestStudent;
    private Long bestGroup;
    private SessionStatus status;
}
