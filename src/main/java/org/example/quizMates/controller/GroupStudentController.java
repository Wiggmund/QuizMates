package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.config.ApplicationConfig;
import org.example.quizMates.dto.group.AddStudentToGroupDto;
import org.example.quizMates.dto.group.RemoveStudentFromGroupDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Student;
import org.example.quizMates.service.GroupStudentService;
import org.example.quizMates.service.impl.GroupStudentServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/groups/students")
@RequiredArgsConstructor
public class GroupStudentController extends HttpServlet {
    private final GroupStudentService groupStudentService;
    private static final Gson gson = ApplicationConfig.GSON;
    private static final String GROUP_ID_REQ_PARAM = "groupId";

    public GroupStudentController() {
        this(GroupStudentServiceImpl.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long requiredId = Long.parseLong(req.getParameter(GROUP_ID_REQ_PARAM));
            List<Student> students = groupStudentService.getAllGroupStudents(requiredId);
            ControllerHelper.writeResponse(resp, students, HttpServletResponse.SC_OK);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            AddStudentToGroupDto addStudentToGroupDto = gson.fromJson(req.getReader(), AddStudentToGroupDto.class);
            groupStudentService.addStudentToGroup(addStudentToGroupDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RemoveStudentFromGroupDto removeStudentFromGroupDto = gson.fromJson(req.getReader(), RemoveStudentFromGroupDto.class);
            groupStudentService.removeStudentFromGroup(removeStudentFromGroupDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }
}
