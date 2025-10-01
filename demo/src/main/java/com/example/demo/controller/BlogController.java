package com.example.demo.controller;

import com.example.demo.model.domain.Article;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor // final 필드 자동 주입
public class BlogController {

    private final BlogService blogService; // 서비스 주입

    @GetMapping("/article_list")
    public String articleList(Model model) {
        // 서비스 계층을 통해 게시글 목록 가져오기
        List<Article> list = blogService.findAll();
        // 모델에 데이터 담아서 뷰에 전달
        model.addAttribute("articles", list);
        // templates/article_list.html 파일 반환
        return "article_list";
    }
}
