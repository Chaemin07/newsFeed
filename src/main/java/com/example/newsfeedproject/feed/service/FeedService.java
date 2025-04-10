package com.example.newsfeedproject.feed.service;

import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.feed.dto.request.FeedRequestDto;
import com.example.newsfeedproject.feed.dto.response.FeedResponseDto;
import com.example.newsfeedproject.feed.entity.NewsFeed;
import com.example.newsfeedproject.feed.repository.NewsFeedRepository;

import com.example.newsfeedproject.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final NewsFeedRepository feedRepository;
    /**
     * 단건 조회 할때 댓글 수, 좋아요 수 추가하고 싶은데 상의 해보기
     * private final CommentRepository commentRepository;
     * private final LikeRepository likeRepository;
     */

    /**
     * 게시글 생성
     */
    @Transactional
    public FeedResponseDto createFeed(User user, FeedRequestDto requestDto) {
        NewsFeed feed = new NewsFeed(user, requestDto.getContent());
        feedRepository.save(feed);
        return toDto(feed);
    }

    /**
     * 게시글 전체 조회 (페이징 + 최신순, 댓글&좋아요는 반영 안됨)
     */
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> getAllFeeds(Pageable pageable) {
        return feedRepository.findAll(pageable)
                .map(this::toDto);
        /**
         * feedRepository.findAll(pageable) -> Page<Feed> 타입
         * 하지만 응답은 Page<FeedResponseDto>이기 때문에
         * Feed 객체를 FeedResponseDto로 바꿔줌
         */
    }

    /**
     * 단일 게시글 조회
     * 단일 게시글 조회 때 좋아요와 댓글 수가 추가될거임 -> 아직 안함
     */
    @Transactional(readOnly = true)
    public FeedResponseDto getFeed(Long id) {
        NewsFeed feed = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        return toDto(feed);
    }


    /**
     * 게시글 수정 - 작성자 본인만 가능
     */
    @Transactional
    public FeedResponseDto updateFeed(Long id, User user, FeedRequestDto requestDto) {
        // 1. 게시글이 존재하지 않을때
        NewsFeed feed = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 2. 작성자가 본인이 아닐때
        if (!feed.getCreator().getId().equals(user.getId())) {
//            throw new IllegalArgumentException("작성자만 수정할 수 있습니다."); -> 에러코드에서 함께 관리
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        feed.updateContents(requestDto.getContent());
        return toDto(feed);
    }

    /**
     * 게시글 삭제 - 작성자 본인만 가능
     */
    @Transactional
    public void deleteFeed(Long id, User user) {
        NewsFeed feed = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!feed.getCreator().getId().equals(user.getId())) {
//            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다."); -> 에러코드에서 함께 관리
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        feedRepository.delete(feed);
    }

    /**
     * 엔티티 → DTO 변환
     */
    private FeedResponseDto toDto(NewsFeed feed) {
        return FeedResponseDto.builder()
                .feedId(feed.getFeedId())
                .creator(feed.getCreator().getNickname())
                .content(feed.getContent())
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .build();
    }
}