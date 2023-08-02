package org.example.quizMates.dto.group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class RemoveStudentFromGroupDto {
    private Long groupId;
    private Long studentId;
}
