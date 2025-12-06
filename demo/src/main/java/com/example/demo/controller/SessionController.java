package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SessionController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String username,
                               HttpSession session,
                               Model model) {

        session.setAttribute("username", username);  // 사용자 이름을 세션에 저장
        model.addAttribute("username", username);

        return "login_success";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 현재 사용자 세션 완전 삭제
        return "logout_success";
    }
}
