package com.example.demo.model.service;

import com.example.demo.model.domain.Member;
import jakarta.validation.constraints.*;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddMemberRequest {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣]+$",
        message = "이름에는 특수문자를 포함할 수 없습니다."
    )
    private String name;

    @NotBlank(message = "이메일은 비워둘 수 없습니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자리여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z]).+$",
        message = "비밀번호는 대문자와 소문자를 모두 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "나이는 필수 입력 항목입니다.")
    @Pattern(
        regexp = "^(19|[2-8][0-9]|90)$",
        message = "나이는 19세 이상 90세 이하만 가능합니다."
    )
    private String age;

    private String mobile;
    private String address;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .age(age)
                .mobile(mobile)
                .address(address)
                .build();
    }
}
