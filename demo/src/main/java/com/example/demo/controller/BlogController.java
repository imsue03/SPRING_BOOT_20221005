package com.example.demo.controller;

import com.example.demo.model.domain.Article;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;


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
    @GetMapping("/article_edit/{id}") // 게시판 링크 지정
    public String article_edit(Model model, @PathVariable Long id) {
        Optional<Article> list = blogService.findById(id); // 선택한 게시판 글
            if (list.isPresent()) {
                model.addAttribute("article", list.get()); // 존재하면 Article 객체를 모델에 추가
                } else {
                // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
                return "/error_page/article_error";  // 오류 처리 페이지로 연결
                }
            return "article_edit"; // .HTML 연결
    }
    @PutMapping("/api/article_edit/{id}")
    public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
        blogService.update(id, request);
        return "redirect:/article_list"; // 글 수정 이후 .html 연결
    }
    @DeleteMapping("/api/article_delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";
    }
}
