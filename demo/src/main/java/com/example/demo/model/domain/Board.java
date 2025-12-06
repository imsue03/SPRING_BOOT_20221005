package com.example.demo.model.domain;

import lombok.*;
import java.time.LocalDate;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title = "";

    @Column(nullable = false)
    private String content = "";

    @Column(nullable = false)
    private String user = "";

    @Column(name = "newdate", nullable = false)
    private LocalDate newdate;   // ← LocalDate로 통일!

    @Column(nullable = false)
    private String count = "";

    @Column(nullable = false)
    private String likec = "";

    @Column(nullable = false)
    private String address = "";

    @Builder
    public Board(String title, String content, String user,
                 LocalDate newdate, String count, String likec, String address) {

        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
        this.address = address;
    }
}
