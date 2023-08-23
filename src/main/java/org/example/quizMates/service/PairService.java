package org.example.quizMates.service;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.model.Pair;
import java.util.List;

public interface PairService extends CrudService<Pair, Long> {
    void createPair(CreatePairDto dto);
    void updatePair(UpdatePairDto dto);
    List<Pair> findPairsByIds(List<Long> ids);
    List<Pair> findPairsByStudents(List<CreatePairDto> dtos);

}
