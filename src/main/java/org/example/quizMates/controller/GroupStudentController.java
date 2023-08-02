package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.group.AddStudentToGroupDto;
import org.example.quizMates.dto.group.RemoveStudentFromGroupDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Student;
import org.example.quizMates.service.GroupStudentService;
import org.example.quizMates.service.impl.GroupStudentServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/groups/students")
@RequiredArgsConstructor
public class GroupStudentController extends HttpServlet {
    private final GroupStudentService groupStudentService;
    private static final Gson gson = new Gson();
    private static final String GROUP_ID_REQ_PARAM = "groupId";

    public GroupStudentController() {
        this(GroupStudentServiceImpl.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            String requiredId = req.getParameter(GROUP_ID_REQ_PARAM);
            List<Student> students = groupStudentService.getAllGroupStudents(Long.parseLong(requiredId));
            writer.println(gson.toJson(students));
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            AddStudentToGroupDto addStudentToGroupDto = gson.fromJson(req.getReader(), AddStudentToGroupDto.class);
            groupStudentService.addStudentToGroup(addStudentToGroupDto);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            RemoveStudentFromGroupDto removeStudentFromGroupDto = gson.fromJson(req.getReader(), RemoveStudentFromGroupDto.class);
            groupStudentService.removeStudentFromGroup(removeStudentFromGroupDto);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }
}
