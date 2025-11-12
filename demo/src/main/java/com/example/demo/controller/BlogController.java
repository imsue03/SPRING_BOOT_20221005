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

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // // 게시글 목록 페이지
    // @GetMapping({"/article_list"})
    // public String articleList(Model model) {
    //     List<Article> list = blogService.findAll();
    //     model.addAttribute("articles", list);
    //     return "article_list"; // templates/article_list.html
    // }

    // 게시글 추가 (폼 제출 시)
    @PostMapping("/articles")
    public String addArticle(@ModelAttribute AddArticleRequest request, RedirectAttributes ra) {
        blogService.save(request); // 게시글 저장
        ra.addFlashAttribute("msg", "게시글이 추가되었습니다.");
        return "redirect:/article_list"; // 저장 후 목록 페이지로 리다이렉트
    }

    // // 게시글 수정 페이지
    // @GetMapping("/article_edit/{id}")
    // public String articleEdit(@PathVariable Long id, Model model) {
    //     Optional<Article> list = blogService.findById(id);
    //     if (list.isPresent()) {
    //         model.addAttribute("article", list.get());
    //     } else {
    //         return "/error_page/article_error";
    //     }
    //     return "article_edit";
    // }

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

    // @GetMapping("/board_list") // 새로운 게시판 링크 지정
    // public String board_list(Model model) {
    //     List<Board> list = blogService.findAll(); // 게시판 전체 리스트, 기존 Article에서 Board로 변경됨
    //     model.addAttribute("boards", list); // 모델에 추가
    //     return "board_list"; // .HTML 연결
    // }

    @GetMapping("/board_list") // 새로운 게시판 링크 지정
    public String board_list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
        PageRequest pageable = PageRequest.of(page, 5); // 한 페이지의 게시글 수
        Page<Board> list; // Page를 반환
        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
            } else {
                list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
                }
                model.addAttribute("boards", list); // 모델에 추가
                model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
                model.addAttribute("currentPage", page); // 페이지 번호
                model.addAttribute("keyword", keyword); // 키워드
                return "board_list"; // .HTML 연결
                }

    @GetMapping("/board_write")
    public String board_write() {
        return "board_write";
    }

    @PostMapping("/api/boards") // 글쓰기 게시판 저장
    public String addboards(@ModelAttribute AddBoardRequest request) {
        blogService.save(request);
        return "redirect:/board_list"; // .HTML 연결
    }


    @GetMapping("/board_view/{id}") // 게시판 링크 지정
    public String board_view(Model model, @PathVariable Long id) {
        Optional<Board> list = blogService.findById(id); // 선택한 게시판 글

        if (list.isPresent()) {
            model.addAttribute("boards", list.get()); // 존재할 경우 실제 Board 객체를 모델에 추가
        } else {
            // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
        return "board_view"; // .HTML 연결
    }

    

}
