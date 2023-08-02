package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.repository.*;
import org.example.quizMates.service.DuplicationService;

@RequiredArgsConstructor
public class DuplicationServiceImpl implements DuplicationService {
    private final StudentRepository studentRepository;
    private final HostRepository hostRepository;
    private final SessionRepository sessionRepository;
    private final GroupRepository groupRepository;
    private final PairRepository pairRepository;

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
