package org.example.quizMates.service.impl;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Pair;
import org.example.quizMates.repository.PairRepository;
import org.example.quizMates.repository.impl.PairRepositoryImpl;
import org.example.quizMates.service.DuplicationService;
import org.example.quizMates.service.PairService;

import java.util.List;

public class PairServiceImpl implements PairService {
    private final PairRepository pairRepository;
    private final DuplicationService duplicationService;
    private final static String PAIR_NOT_FOUND = "Pair with id %s not found";
    private final static String THE_SAME_PAIR_EXIST = "The same pair with studentId %s and studentId %s has already existed";

    private PairServiceImpl() {
        this.pairRepository = PairRepositoryImpl.getInstance();
        this.duplicationService = DuplicationServiceImpl.getInstance();
    }

    private static class PairServiceSingleton {
        private static final PairService INSTANCE = new PairServiceImpl();
    }

    public static PairService getInstance() {
        return PairServiceSingleton.INSTANCE;
    }

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
        boolean theSamePairExist = duplicationService.doTheSamePairExist(dto.getStudentA(), dto.getStudentB());
        if (theSamePairExist) {
            throw new ResourceNotFoundException(String.format(THE_SAME_PAIR_EXIST, dto.getStudentA(), dto.getStudentB()));
        }
        pairRepository.createPair(dto);
    }

    @Override
    public void updatePair(UpdatePairDto dto) {
        boolean theSamePairExist = duplicationService.doTheSamePairExist(dto.getStudentA(), dto.getStudentB());
        if (theSamePairExist) {
            throw new ResourceNotFoundException(String.format(THE_SAME_PAIR_EXIST, dto.getStudentA(), dto.getStudentB()));
        }
        findById(dto.getId());
        pairRepository.updatePair(dto);
    }
}
