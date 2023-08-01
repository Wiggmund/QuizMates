package org.example.quizMates.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.db.PostgreSQLConfig;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Pair;
import org.example.quizMates.repository.impl.PairRepositoryImpl;
import org.example.quizMates.service.PairService;
import org.example.quizMates.service.impl.PairServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet("/pairs")
@RequiredArgsConstructor
public class PairController extends HttpServlet {
    private final PairService pairService;

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
            String id = req.getParameter("id");
            if (id == null) {
                List<Pair> allPairs = pairService.findAll();
      //          req.setAttribute("pairs", allPairs);
            writer.println(new Gson().toJson(allPairs));
            } else {
                Optional<Pair> byId = pairService.findById(Long.parseLong(id));
                writer.println(new Gson().toJson(byId));
            }
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            JsonObject jsonObject = new Gson().fromJson(req.getReader(), JsonObject.class);
            CreatePairDto createPairDto = new Gson().fromJson(jsonObject, CreatePairDto.class);

            Pair pair = pairService.createPair(createPairDto);
            writer.println(new Gson().toJson(pair));
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            writer.println("Your JSON response");
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            writer.println("Your JSON response");
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }
}
