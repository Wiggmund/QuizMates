package org.example.quizMates.service;

import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.model.Group;

public interface GroupService extends CrudService<Group, Long> {
    void createGroup(CreateGroupDto dto);
    void updateGroup(UpdateGroupDto dto);
}
