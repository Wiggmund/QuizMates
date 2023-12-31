package org.example.quizMates.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
}
