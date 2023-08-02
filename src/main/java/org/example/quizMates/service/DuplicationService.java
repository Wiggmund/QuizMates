package org.example.quizMates.service;

public interface DuplicationService {
    boolean doTheSameStudentExist(String firstName, String lastName);
    boolean doTheSameHostExist(String firstName, String lastName);
    boolean doTheSameSessionExist(String title);
    boolean doTheSameGroupExist(String name);
    boolean doTheSamePairExist(Long studentA, Long studentB);
}
