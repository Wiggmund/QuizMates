package org.example.quizMates.service;

import org.example.quizMates.dto.http.GeneratePairsRequestDto;
import org.example.quizMates.dto.http.GeneratePairsResponseDto;
import org.example.quizMates.model.Pair;

import java.util.List;

public interface StudentsPairsService {
    GeneratePairsResponseDto generatePairs(GeneratePairsRequestDto payload);
}
