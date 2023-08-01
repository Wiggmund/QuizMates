package org.example.quizMates.service;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.model.Pair;

public interface PairService extends CrudService<Pair, Long> {
    void createPair(CreatePairDto dto);
    void updatePair(UpdatePairDto dto);
}
