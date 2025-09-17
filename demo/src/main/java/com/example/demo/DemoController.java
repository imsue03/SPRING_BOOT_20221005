package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 컨트롤러 어노테이션 명시
public class DemoController {
    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", " 방갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }
    @GetMapping("/hello2") // 전송 방식 GET
    public String hello2(Model model) {
        model.addAttribute("data1", " 이수헌님.");
        model.addAttribute("data2", " 방갑습니다.");
        model.addAttribute("data3", " 오늘.");
        model.addAttribute("data4", " 날씨는.");
        model.addAttribute("data5", " 비가옵니다."); // model 설정
        return "hello2"; // hello2.html 연결
    }
    @GetMapping("/about_detailed") // 전송 방식 GET
    public String about(Model model) {
        return "about_detailed"; // about_detailed.html 연결
    }
}