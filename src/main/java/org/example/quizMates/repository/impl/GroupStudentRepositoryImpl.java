package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.group.AddStudentToGroupDto;
import org.example.quizMates.dto.group.RemoveStudentFromGroupDto;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.GroupStudentRepository;

import java.util.List;

public class GroupStudentRepositoryImpl implements GroupStudentRepository {
    private final static String STUDENT_TABLE_NAME = "students";
    private final static String STUDENT_ID_COL = "id";
    private final static String STUDENT_FIRST_NAME_COL = "first_name";
    private final static String STUDENT_LAST_NAME_COL = "last_name";
    private final static String STUDENT_GROUP_ID_COL = "group_id";
    private final DBConnection dbConnection;

    private GroupStudentRepositoryImpl() {
        this.dbConnection = DBConnectionDriverManager.getInstance();
    }

    private static class GroupStudentRepositoryImplSingleton {
        private static final GroupStudentRepository INSTANCE = new GroupStudentRepositoryImpl();
    }

    public static GroupStudentRepository getInstance() {
        return GroupStudentRepositoryImplSingleton.INSTANCE;
    }

    @Override
    public List<Student> getAllGroupStudents() {
        return null;
    }

    @Override
    public void addStudentToGroup(AddStudentToGroupDto dto) {

    }

    @Override
    public void removeStudentFromGroup(RemoveStudentFromGroupDto dto) {

    }
}
