package com.example.demo.controller;

import com.example.demo.model.domain.Article;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // 게시글 목록 페이지
    @GetMapping({"/article_list"})
    public String articleList(Model model) {
        List<Article> list = blogService.findAll();
        model.addAttribute("articles", list);
        return "article_list"; // templates/article_list.html
    }

    // 게시글 추가 (폼 제출 시)
    @PostMapping("/articles")
    public String addArticle(@ModelAttribute AddArticleRequest request, RedirectAttributes ra) {
        blogService.save(request); // 게시글 저장
        ra.addFlashAttribute("msg", "게시글이 추가되었습니다.");
        return "redirect:/article_list"; // 저장 후 목록 페이지로 리다이렉트
    }

    // 게시글 수정 페이지
    @GetMapping("/article_edit/{id}")
    public String articleEdit(@PathVariable Long id, Model model) {
        Optional<Article> list = blogService.findById(id);
        if (list.isPresent()) {
            model.addAttribute("article", list.get());
        } else {
            return "/error_page/article_error";
        }
        return "article_edit";
    }

    // 게시글 수정 처리
    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
        blogService.update(id, request);
        return "redirect:/article_list";
    }

    // 게시글 삭제 처리
    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";
    }

    // 잘못된 게시판 접근 (예: /article_edit/abcd)
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
