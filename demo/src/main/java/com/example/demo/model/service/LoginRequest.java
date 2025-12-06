package com.example.demo.model.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
    private String rememberMe;   // 체크박스 값 ("on" / null)
}
