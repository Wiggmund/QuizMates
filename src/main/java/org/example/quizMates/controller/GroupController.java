package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.example.quizMates.config.ApplicationConfig;
import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Group;

import org.example.quizMates.service.GroupService;
import org.example.quizMates.service.impl.GroupServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/groups")
@RequiredArgsConstructor
public class GroupController extends HttpServlet {
    private final GroupService groupService;
    private final static Gson gson = ApplicationConfig.GSON;
    private final static String ID_REQ_PARAM = "id";

    public GroupController() {
        this(GroupServiceImpl.gteInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requiredId = req.getParameter(ID_REQ_PARAM);

            if (requiredId == null || requiredId.isEmpty()) {
                List<Group> groups = groupService.findAll();
                ControllerHelper.writeResponse(resp, groups, HttpServletResponse.SC_OK);
            } else {
                Group group = groupService.findById(Long.parseLong(requiredId));
                ControllerHelper.writeResponse(resp, group, HttpServletResponse.SC_OK);
            }
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CreateGroupDto dto = gson.fromJson(req.getReader(), CreateGroupDto.class);
            groupService.createGroup(dto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UpdateGroupDto dto = gson.fromJson(req.getReader(), UpdateGroupDto.class);
            groupService.updateGroup(dto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long requiredId = Long.parseLong(req.getParameter(ID_REQ_PARAM));
            groupService.deleteById(requiredId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }
}
