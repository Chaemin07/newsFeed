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
     * 팔로잉한 사람들 게시글(최신순, 페이징)
     *
     * @param creatorIds : List<Long> > 게시글을 작성한 사용자들의 ID 목록 (내가 팔로우한 유저들)
     * @param pageable : Pageable > 페이징 및 정렬 정보 (몇 페이지를 조회할지, 몇 개씩 가져올지 등)
     * @return Page<NewsFeed> : 게시글을 담고 있는 페이지 객체 > 내가 팔로우한 사람들의 게시글 목록 (최신순 정렬, 페이징 포함)
     */
    Page<NewsFeed> findByCreatorIdInOrderByCreatedAtDesc(List<Long> creatorIds, Pageable pageable);

    /**
     * 특정 사용자가 작성한 게시글(최신순, 페이징).
     *
     * @param creatorId 게시글 작성자(사용자)의 고유 ID
     * @param pageable 페이지 번호, 크기, 정렬 기준이 담긴 객체
     * @return 사용자가 작성한 게시글 목록 (최신순 정렬, 페이징 포함)
     */
    Page<NewsFeed> findByCreatorIdOrderByCreatedAtDesc(Long creatorId, Pageable pageable);
}