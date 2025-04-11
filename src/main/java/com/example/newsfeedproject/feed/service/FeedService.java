package com.example.newsfeedproject.feed.service;

import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.feed.dto.request.FeedRequestDto;
import com.example.newsfeedproject.feed.dto.response.FeedResponseDto;
import com.example.newsfeedproject.feed.entity.NewsFeed;
import com.example.newsfeedproject.feed.repository.NewsFeedRepository;

import com.example.newsfeedproject.like.dto.followdto.FollowListResponseDto;
import com.example.newsfeedproject.like.service.followservice.FollowService;
import com.example.newsfeedproject.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final NewsFeedRepository feedRepository;
    private final FollowService followService;

    /**
     * 단건 조회 할때 댓글 수, 좋아요 수 추가하고 싶은데 상의 해보기
     * private final CommentRepository commentRepository;
     * private final LikeRepository likeRepository;
     */

    /**
     * 게시글을 생성, 저장
     *
     * 로그인 유저의 피드 내용 -> 생성 -> 응답 Dto로 반환
     *
     * @param user 로그인 유저(작성자)
     * @param requestDto 피드 생성 요청 데이터 (내용 포함)
     * @return 피드 응답 DTO
     */
    @Transactional
    public FeedResponseDto createFeed(User user, FeedRequestDto requestDto) {
        NewsFeed feed = new NewsFeed(user, requestDto.getContent());
        feedRepository.save(feed);
        return toDto(feed);
    }

    /**
     * 로그인 유저 피드 전체(최신순, 페이징) -> 인스타 본인 프로필이라고 생각하면 편함
     *
     * @param loginUser 현재 로그인한 사용자
     * @param pageable 페이지 번호, 크기, 정렬 정보
     * @return 본인이 작성한 피드 목록
     */
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> getMyFeeds(User loginUser, Pageable pageable) {
        Page<NewsFeed> myFeeds = feedRepository.findByCreatorIdOrderByCreatedAtDesc(
                loginUser.getId(), pageable
        );
        return myFeeds.map(this::toDto);
    }

    /**
     * 본인 + 팔로잉한 사람들의 피드(최신순, 페이징)
     *
     * @param loginUser 현재 로그인한 사용자
     * @param pageable 페이징
     * @return 로그인 유저 + 팔로우한 유저들의 피드 목록
     */
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> getFeedsByFollowing(User loginUser, Pageable pageable) {
        // 1. 팔로잉한 사람 정보 불러오기(DTO 리스트로)
        List<FollowListResponseDto> followingList = followService.getFollowing(loginUser.getId());

        // 2. ID만 추출 -> followservice에 id 조회하는게 없어서
        List<Long> followingIds = followingList.stream()
                .map(FollowListResponseDto::getId)// Dto에서 추출
                .collect(Collectors.toList());
        // 3. 내 글도 포함되게 내 ID 추가
        followingIds.add(loginUser.getId());
        // 4. 최신순으로 전체 조회
        Page<NewsFeed> feeds = feedRepository.findByCreatorIdInOrderByCreatedAtDesc(followingIds, pageable);
        return feeds.map(this::toDto);
    }

    /**
     * 게시글 ID -> 단일 피드 조회
     * 없는 피드 ID의 경우 예외 발생 -> 응답 Dto로 반환
     *
     * @param id 조회할 피드 ID
     * @return 피드의 응답 DTO
     */
    @Transactional(readOnly = true)
    public FeedResponseDto getFeed(Long id) {
        NewsFeed feed = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("피드를 찾을 수 없습니다."));
        return toDto(feed);
    }


    /**
     * 피드 수정 (본인만)
     *
     * 피드 ID/로그인유저 정보 비교 -> 작성자일 경우에만 수정 / 아니면 예외처리
     *
     * @param id 수정할 피드 고유 ID
     * @param user 현재 로그인 유저
     * @param requestDto 수정할 피드 내용(요청 DTO)
     * @return 수정된 피드 (응답 DTO)
     */
    @Transactional
    public FeedResponseDto updateFeed(Long id, User user, FeedRequestDto requestDto) {
        // 1. 피드가 존재하지 않을때
        NewsFeed feed = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("피드를 찾을 수 없습니다."));

        // 2. 작성자가 본인이 아닐때
        if (!feed.getCreator().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        feed.updateContents(requestDto.getContent());
        return toDto(feed);
    }

    /**
     * 피드 삭제 (본인만)
     *
     * 피드 ID와 로그인 유저 비교 -> 본인일 경우만 삭제 / 아니면 예외처리
     *
     * @param id 삭제할 피드 고유 ID
     * @param user 현재 로그인한 유저
     */
    @Transactional
    public void deleteFeed(Long id, User user) {
        NewsFeed feed = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!feed.getCreator().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        feedRepository.delete(feed);
    }

    /**
     * NewsFeed 엔티티 -> FeedResponseDto로 변환
     *
     * @param feed 변환할 NewsFeed 엔티티
     * @return 응답용 DTO
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