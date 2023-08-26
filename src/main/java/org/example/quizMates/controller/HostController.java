package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.config.ApplicationConfig;
import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Host;
import org.example.quizMates.service.HostService;
import org.example.quizMates.service.impl.HostServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/hosts")
@RequiredArgsConstructor
public class HostController extends HttpServlet {
    private final HostService hostService;
    private static final Gson gson = ApplicationConfig.GSON;
    private static final String ID_REQ_PARAM = "hostId";

    public HostController() {
        this(HostServiceImpl.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requiredId = req.getParameter(ID_REQ_PARAM);

            if(requiredId == null || requiredId.isEmpty()) {
                List<Host> hosts = hostService.findAll();
                ControllerHelper.writeResponse(resp, hosts, HttpServletResponse.SC_OK);
            } else {
                Host host = hostService.findById(Long.parseLong(requiredId));
                ControllerHelper.writeResponse(resp, host, HttpServletResponse.SC_OK);
            }
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CreateHostDto createHostDto = gson.fromJson(req.getReader(), CreateHostDto.class);
            hostService.createHost(createHostDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UpdateHostDto updateHostDto = gson.fromJson(req.getReader(), UpdateHostDto.class);
            hostService.updateHost(updateHostDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String hostId = req.getParameter(ID_REQ_PARAM);
            hostService.deleteById(Long.parseLong(hostId));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }
}
