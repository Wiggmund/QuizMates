package org.example.quizMates.repository;

import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {
    void createGroup(CreateGroupDto dto);
    void updateGroup(UpdateGroupDto dto);
}
