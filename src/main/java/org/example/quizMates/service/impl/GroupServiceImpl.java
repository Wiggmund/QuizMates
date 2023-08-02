package org.example.quizMates.service.impl;

import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.exception.ResourceAlreadyExistException;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Group;
import org.example.quizMates.repository.GroupRepository;
import org.example.quizMates.repository.impl.GroupRepositoryImpl;
import org.example.quizMates.service.DuplicationService;
import org.example.quizMates.service.GroupService;

import java.util.List;

public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final DuplicationService duplicationService;
    private final static String GROUP_DUPLICATE_EXCEPTION = "Group with name %s already exist";

    private GroupServiceImpl() {
        this.groupRepository = GroupRepositoryImpl.getInstance();
        this.duplicationService = DuplicationServiceImpl.getInstance();
    }

    private static class GroupServiceSingleton {
        private static final GroupService INSTANCE = new GroupServiceImpl();
    }

    public static GroupService gteInstance() {
        return GroupServiceSingleton.INSTANCE;
    }

    private final static String GROUP_NOT_FOUND = "Group with id %s not found";
    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(GROUP_NOT_FOUND, id)));
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
        boolean doTheSameGroupExist = duplicationService.doTheSameGroupExist(dto.getName());

        if (doTheSameGroupExist) {
            throw new ResourceAlreadyExistException(String.format(GROUP_DUPLICATE_EXCEPTION, dto.getName()));
        }

        groupRepository.createGroup(dto);
    }

    @Override
    public void updateGroup(UpdateGroupDto dto) {
        boolean doTheSameGroupExist = duplicationService.doTheSameGroupExist(dto.getName());

        if (doTheSameGroupExist) {
            throw new ResourceAlreadyExistException(String.format(GROUP_DUPLICATE_EXCEPTION, dto.getName()));
        }

        findById(dto.getId());
        groupRepository.updateGroup(dto);
    }
}
