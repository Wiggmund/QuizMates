package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.config.ApplicationConfig;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;

import org.example.quizMates.model.Pair;
import org.example.quizMates.service.PairService;
import org.example.quizMates.service.impl.PairServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/pairs")
@RequiredArgsConstructor
public class PairController extends HttpServlet {
    private final PairService pairService;
    private final static Gson gson = ApplicationConfig.GSON;
    private final static String ID_REQ_PARAM = "id";

    public PairController() {
        this(PairServiceImpl.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter(ID_REQ_PARAM);
            if (id == null || id.isEmpty()) {
                List<Pair> allPairs = pairService.findAll();
                ControllerHelper.writeResponse(resp, allPairs, HttpServletResponse.SC_OK);
            } else {
                Pair pair = pairService.findById(Long.parseLong(id));
                ControllerHelper.writeResponse(resp, pair, HttpServletResponse.SC_OK);
            }
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CreatePairDto createPairDto = gson.fromJson(req.getReader(), CreatePairDto.class);
            pairService.createPair(createPairDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UpdatePairDto updatePairDto = gson.fromJson(req.getReader(), UpdatePairDto.class);
            pairService.updatePair(updatePairDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter(ID_REQ_PARAM));
            pairService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }
}
