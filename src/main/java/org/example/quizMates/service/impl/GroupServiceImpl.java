package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Group;
import org.example.quizMates.repository.GroupRepository;
import org.example.quizMates.service.GroupService;

import java.util.List;

@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final static String USER_NOT_FOUND = "User with id %s not found";
    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        groupRepository.deleteById(id);
    }

    @Override
    public void createGroup(CreateGroupDto dto) {
        groupRepository.createGroup(dto);
    }

    @Override
    public void updateGroup(UpdateGroupDto dto) {
        findById(dto.getId());
        groupRepository.updateGroup(dto);
    }
}
