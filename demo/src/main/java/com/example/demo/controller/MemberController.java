package com.example.demo.controller;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.LoginRequest;
import com.example.demo.model.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    // 회원가입 페이지
    @GetMapping("/join_new")
    public String join_new() {
        return "join_new";
    }

    // 회원가입 완료 처리
    @PostMapping("/api/members")
    public String addmembers(
            @Valid @ModelAttribute AddMemberRequest request,
            BindingResult bindingResult, 
            Model model) {

        if (bindingResult.hasErrors()) {
            // 오류 메시지 가져오기
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "join_new";  // 다시 회원가입 화면으로 이동
        }

        memberService.saveMember(request);

        return "join_end"; // 성공 페이지
    }

    // 로그인 페이지
    @GetMapping({"/login", "/member_login"})
        public String member_login() {
            return "login";
        }

        // 로그인 체크
        @PostMapping("/api/login_check")
    public String checkMembers(
            @ModelAttribute AddMemberRequest request,
            Model model,
            HttpServletRequest req,
            HttpServletResponse res) {

        try {
            // 로그인 검증
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());

            // 기존 세션 무효화
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();

            // 새로운 세션 생성
            HttpSession session = req.getSession(true);

            // ⭐ 사용자마다 다른 세션 ID를 직접 생성해서 넣기 ⭐
            String customSessionKey = "USER_SESSION_" + UUID.randomUUID();
            session.setAttribute("sessionKey", customSessionKey);

            // 유저 정보 저장
            session.setAttribute("userId", member.getId());
            session.setAttribute("email", member.getEmail());

            return "redirect:/board_list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }


    // 로그아웃
    @GetMapping("/api/logout")
    public String member_logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();

            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "login";
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files,
                            Model model) {

        List<String> savedNames = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                // ⭐ 동일 파일명 업로드 시 UUID로 새 이름 생성 ⭐
                String newName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                // 저장 경로
                String path = "C:/uploads/" + newName;

                // 파일 저장
                file.transferTo(new File(path));

                savedNames.add(newName);
            }

            model.addAttribute("files", savedNames);
            return "board_list";

        } catch (Exception e) {
            model.addAttribute("error", "파일 업로드 실패: " + e.getMessage());
            return "article_error";
        }
    }
}
