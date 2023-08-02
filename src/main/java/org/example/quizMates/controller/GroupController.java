package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.db.PostgreSQLConfig;

import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Group;

import org.example.quizMates.repository.impl.*;
import org.example.quizMates.service.GroupService;
import org.example.quizMates.service.impl.DuplicationServiceImpl;
import org.example.quizMates.service.impl.GroupServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/groups")
@RequiredArgsConstructor
public class GroupController extends HttpServlet {
    private final GroupService groupService;
    private final static Gson gson = new Gson();
    private final static String ID_REQ_PARAM = "id";

    public GroupController() {
        this(new GroupServiceImpl(
                new GroupRepositoryImpl(new DBConnectionDriverManager(new PostgreSQLConfig())),
                new DuplicationServiceImpl(
                        new StudentRepositoryImpl(new DBConnectionDriverManager(new PostgreSQLConfig())),
                        new HostRepositoryImpl(new DBConnectionDriverManager(new PostgreSQLConfig())),
                        new SessionRepositoryImpl(new DBConnectionDriverManager(new PostgreSQLConfig())),
                        new GroupRepositoryImpl(new DBConnectionDriverManager(new PostgreSQLConfig())),
                        new PairRepositoryImpl(new DBConnectionDriverManager(new PostgreSQLConfig()))))
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            String requiredId = req.getParameter(ID_REQ_PARAM);

            if (requiredId == null || requiredId.isEmpty()) {
                List<Group> groups = groupService.findAll();
                writer.println(gson.toJson(groups));
            } else {
                Group group = groupService.findById(Long.parseLong(requiredId));
                writer.println(gson.toJson(group));
            }
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
            CreateGroupDto dto = gson.fromJson(req.getReader(), CreateGroupDto.class);
            groupService.createGroup(dto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            UpdateGroupDto dto = gson.fromJson(req.getReader(), UpdateGroupDto.class);
            groupService.updateGroup(dto);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
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
            long requiredId = Long.parseLong(req.getParameter(ID_REQ_PARAM));
            groupService.deleteById(requiredId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }
}
