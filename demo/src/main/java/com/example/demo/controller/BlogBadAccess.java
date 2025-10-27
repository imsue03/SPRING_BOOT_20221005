package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice(annotations = Controller.class)
public class BlogBadAccess {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badAccess(MethodArgumentTypeMismatchException ex,
                            HttpServletRequest req,
                            Model model) {
        model.addAttribute("title", "블로그 게시판 - 잘못된 접근");
        model.addAttribute("path", req.getRequestURI());
        model.addAttribute("input", ex.getValue());
        return "error_page/article_bad_access";  
    }
}
