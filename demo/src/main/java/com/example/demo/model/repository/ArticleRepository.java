package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.domain.Article;

/**
 * 게시글 엔티티용 JPA Repository
 * → 기본 CRUD(findAll, save 등) 메서드 자동 제공
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
    // 여기에는 메서드 본문이 없어야 함!
    // 필요한 경우 추상 메서드 시그니처만 선언 가능
}
