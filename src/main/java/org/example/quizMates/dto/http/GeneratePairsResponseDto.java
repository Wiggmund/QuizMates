package org.example.quizMates.dto.http;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.quizMates.model.Pair;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class GeneratePairsResponseDto {
    private List<Pair> pairs;
    private List<Long> unpairedStudentsIds;
}
