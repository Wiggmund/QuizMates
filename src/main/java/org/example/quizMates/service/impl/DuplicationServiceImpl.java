package org.example.quizMates.service.impl;

import org.example.quizMates.repository.*;
import org.example.quizMates.repository.impl.*;
import org.example.quizMates.service.DuplicationService;


public class DuplicationServiceImpl implements DuplicationService {
    private final StudentRepository studentRepository;
    private final HostRepository hostRepository;
    private final SessionRepository sessionRepository;
    private final GroupRepository groupRepository;
    private final PairRepository pairRepository;

    private DuplicationServiceImpl() {
        this.studentRepository = StudentRepositoryImpl.getInstance();
        this.hostRepository = HostRepositoryImpl.getInstance();
        this.sessionRepository = SessionRepositoryImpl.getInstance();
        this.groupRepository = GroupRepositoryImpl.getInstance();
        this.pairRepository = PairRepositoryImpl.getInstance();
    }

    private static class DuplicationServiceSingleton {
        private static final DuplicationService INSTANCE = new DuplicationServiceImpl();
    }
    public static DuplicationService getInstance() {
        return DuplicationServiceSingleton.INSTANCE;
    }

    @Override
    public boolean doTheSameStudentExist(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName).isPresent();
    }

    @Override
    public boolean doTheSameHostExist(String firstName, String lastName) {
        return hostRepository.findByFirstNameAndLastName(firstName, lastName).isPresent();
    }

    @Override
    public boolean doTheSameSessionExist(String title) {
        return sessionRepository.findByTitle(title).isPresent();
    }

    @Override
    public boolean doTheSameGroupExist(String name) {
        return groupRepository.findByName(name).isPresent();
    }

    @Override
    public boolean doTheSamePairExist(Long studentA, Long studentB) {
        return pairRepository.findByStudentAAndStudentB(studentA, studentB).isPresent();
    }
}
