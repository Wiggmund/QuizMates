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
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Pair;
import org.example.quizMates.repository.impl.PairRepositoryImpl;
import org.example.quizMates.service.PairService;
import org.example.quizMates.service.impl.PairServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/pairs")
@RequiredArgsConstructor
public class PairController extends HttpServlet {
    private final PairService pairService;
    private final static Gson gson = new Gson();
    private final static String ID_REQ_PARAM = "id";

    public PairController() {
        this(new PairServiceImpl(
                new PairRepositoryImpl(
                        new DBConnectionDriverManager(
                                new PostgreSQLConfig()))));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            String id = req.getParameter(ID_REQ_PARAM);
            if (id == null || id.isEmpty()) {
                List<Pair> allPairs = pairService.findAll();
                writer.println(gson.toJson(allPairs));
            } else {
                Pair pair = pairService.findById(Long.parseLong(id));
                writer.println(gson.toJson(pair));
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
            CreatePairDto createPairDto = gson.fromJson(req.getReader(), CreatePairDto.class);
            pairService.createPair(createPairDto);
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
            UpdatePairDto updatePairDto = gson.fromJson(req.getReader(), UpdatePairDto.class);
            pairService.updatePair(updatePairDto);
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
            Long id = Long.parseLong(req.getParameter(ID_REQ_PARAM));
            pairService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }
}
