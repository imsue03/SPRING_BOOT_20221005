package com.example.demo.controller;

// import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.AddBoardRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.nio.file.Path;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.beans.factory.annotation.Value;


@Controller
public class FileController {

    @Autowired

    @Value("${spring.servlet.multipart.location}") // properties 등록된 설정(경로) 주입
    private String uploadFolder;

    @PostMapping("/upload-email")
    public String uploadEmail(
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            RedirectAttributes redirectAttributes) {

        try {
            Path uploadPath = Paths.get(uploadFolder).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
            Path filePath = uploadPath.resolve(sanitizedEmail + ".txt");
            System.out.println("File path: " + filePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                writer.write("메일 제목: " + subject);
                writer.newLine();
                writer.write("요청 메시지:");
                writer.newLine();
                writer.write(message);
            }

            redirectAttributes.addFlashAttribute("message", "메일 내용이 성공적으로 업로드되었습니다!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "업로드 중 오류가 발생했습니다.");
        }

        return "redirect:/";
    }
}