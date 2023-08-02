package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Pair;
import org.example.quizMates.repository.PairRepository;
import org.example.quizMates.service.DuplicationService;
import org.example.quizMates.service.PairService;

import java.util.List;

@RequiredArgsConstructor
public class PairServiceImpl implements PairService {
    private final PairRepository pairRepository;
    private final DuplicationService duplicationService;

    private final static String PAIR_NOT_FOUND= "Pair with id %s not found";

    @Override
    public Pair findById(Long id) {
        return pairRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(PAIR_NOT_FOUND, id)));
    }

    @Override
    public List<Pair> findAll() {
        return pairRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        pairRepository.deleteById(id);
    }

    @Override
    public void createPair(CreatePairDto dto) {
        pairRepository.createPair(dto);
    }

    @Override
    public void updatePair(UpdatePairDto dto) {
        findById(dto.getId());
        pairRepository.updatePair(dto);
    }
}
