package com.example.newsfeedproject.Feed.repository;

import com.example.newsfeedproject.Feed.entity.NewsFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {
}
/**
 * interface 선언 시 -> Spring Data JPA가 구현체를 만들어서 빈으로 등록
 */