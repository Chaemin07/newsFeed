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
     * 단건 조회 할때 댓글 수, 좋아요 수 추가하고 싶은데 상의 해보기 -> 시간 없음...!
     * private final CommentRepository commentRepository;
     * private final LikeRepository likeRepository;
     */

    /**
     * 피드 생성, 저장
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
     * 피드 전체 조회(특정 유저, 로그인유저+친구)
     * userId != null -> 해당 유저 피드 조회(최신순)
     * userId == null -> 로그인 유저 + 친구들 피드(최신순)
     *
     * @param loginUser 로그인 유저
     * @param userId 조회할 유저 id
     * @param pageable 페이징
     * @return 페이징된 게시글 목록
     */
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> getFeeds(User loginUser, Long userId, Pageable pageable) {

        // 유저 id 입력 여부 확인
        if (userId != null) {
            return feedRepository.findByCreatorIdOrderByCreatedAtDesc(userId, pageable)
                    .map(this::toDto);
        }

        // 로그인 유저의 id에서 팔로우 불러오기 -> List로 변환
        List<FollowListResponseDto> followingList = followService.getFollowing(loginUser.getId());

        // 팔로우 List에 있는 팔로우id 가져오기 -> Long타입 List로 변환
        List<Long> followingIds = followingList.stream()
                .map(FollowListResponseDto::getId)
                .collect(Collectors.toList());

        // 로그인 유저의 게시글도 포함되기 때문에 List에 유저 추가하기
        followingIds.add(loginUser.getId());

        //List에 포함된 모든 사람들의 글 반환
        return feedRepository.findByCreatorIdInOrderByCreatedAtDesc(followingIds, pageable)
                .map(this::toDto);
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