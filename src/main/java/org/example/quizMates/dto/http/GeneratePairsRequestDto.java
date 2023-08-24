package org.example.quizMates.dto.http;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class GeneratePairsRequestDto {
    private List<Long> groupsIds;
    private List<Long> absentStudents;
}
