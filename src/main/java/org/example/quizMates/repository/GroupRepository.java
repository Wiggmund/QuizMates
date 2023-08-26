package org.example.quizMates.repository;

import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupStudentAmount;
import org.example.quizMates.model.Group;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group, Long> {
    Optional<Group> findByName(String name);
    void createGroup(CreateGroupDto dto);
    void updateGroup(UpdateGroupDto dto);
    void updateGroupStudentAmount(UpdateGroupStudentAmount dto);
}
