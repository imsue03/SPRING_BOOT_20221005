package com.example.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
    public String addmembers(@ModelAttribute AddMemberRequest request) {
        memberService.saveMember(request);
        return "redirect:/join_end";
    }

    @GetMapping("/join_end")
    public String join_end() {
        return "join_end";
    }

    // 로그인 페이지
    @GetMapping({"/login", "/member_login"})
    public String member_login() {
        return "login";
    }

    // 로그인 체크
    @PostMapping("/api/login_check")
    public String checkMembers(@ModelAttribute AddMemberRequest request,
                               Model model,
                               HttpServletRequest req,
                               HttpServletResponse res,
                               HttpSession session) {

        try {
            // 이메일/비밀번호 확인
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());

            // 기존 세션 제거
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();

                // JSESSIONID 쿠키 삭제
                Cookie delCookie = new Cookie("JSESSIONID", null);
                delCookie.setPath("/");
                delCookie.setMaxAge(0);
                res.addCookie(delCookie);
            }

            // 새로운 세션 생성
            session = req.getSession(true);
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
    public String member_logout(Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        try {
            // 기존 세션 무효화
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();

                // JSESSIONID 쿠키 삭제
                Cookie cookie = new Cookie("JSESSIONID", null);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }

            // 새로운 빈 세션 생성(선택)
            request.getSession(true);

            return "login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}
