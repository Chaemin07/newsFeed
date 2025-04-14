package com.example.newsfeedproject.feed.repository;

import com.example.newsfeedproject.feed.entity.NewsFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {

    /**
     * Id List 기준 -> 작성한 게시글 목록(최신순, 페이징)
     *
     * @param creatorIds 조회하려는 사용자 Id 목록 -> List 타입
     * @param pageable 페이징
     * @return 뉴스피드 목록(최신순)
     */
    Page<NewsFeed> findByCreatorIdInOrderByCreatedAtDesc(List<Long> creatorIds, Pageable pageable);

    /**
     * Id 기준 -> 작성한 게시글 목록(최신순, 페이징)
     *
     * @param creatorId 조회하려는 Id -> Long 타입
     * @param pageable 페이징
     * @return 뉴스피드 목록(최신순)
     */
    Page<NewsFeed> findByCreatorIdOrderByCreatedAtDesc(Long creatorId, Pageable pageable);
}