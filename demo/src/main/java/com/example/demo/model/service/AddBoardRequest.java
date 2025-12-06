package com.example.demo.model.service;

import java.time.LocalDate;   // ← 반드시 필요!!

import lombok.Data;
import com.example.demo.model.domain.Board;

@Data
public class AddBoardRequest {

    private String title;
    private String content;
    private String user;
    private String count;
    private String likec;
    private String address;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .newdate(LocalDate.now())  // 오늘 날짜 자동 입력
                .count(count)
                .likec(likec)
                .address(address)
                .build();
    }
}
