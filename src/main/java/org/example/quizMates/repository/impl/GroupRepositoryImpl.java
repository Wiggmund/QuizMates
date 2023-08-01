package org.example.quizMates.repository.impl;

import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.model.Group;
import org.example.quizMates.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

public class GroupRepositoryImpl implements GroupRepository {
    @Override
    public Optional<Group> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Group> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void createGroup(CreateGroupDto dto) {

    }

    @Override
    public void updateGroup(UpdateGroupDto dto) {

    }
}
