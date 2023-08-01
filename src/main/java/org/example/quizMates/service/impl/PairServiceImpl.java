package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.model.Pair;
import org.example.quizMates.repository.PairRepository;
import org.example.quizMates.service.PairService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PairServiceImpl implements PairService {
    private final PairRepository pairRepository;

    @Override
    public Optional<Pair> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Pair> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public CreatePairDto createPair(CreatePairDto dto) {
        pairRepository.createPair(dto);
        return dto;
    }

    @Override
    public void updatePair(UpdatePairDto dto) {

    }
}
