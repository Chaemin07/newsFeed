package com.example.newsfeedproject.feed.controller;

import com.example.newsfeedproject.auth.dto.LoginResponseDto;
import com.example.newsfeedproject.common.response.ApiResponse;
import com.example.newsfeedproject.feed.dto.request.FeedRequestDto;
import com.example.newsfeedproject.feed.dto.response.FeedResponseDto;
import com.example.newsfeedproject.feed.service.FeedService;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.newsfeedproject.auth.SessionManager.LOGIN_USER;

@RestController
@RequestMapping("/newsfeeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final UserService userService;

    // httpsession session 사용하는 반복적인 코드를 메서드로 설정함
    private User getLoginUser(HttpSession session) {
        LoginResponseDto dto = (LoginResponseDto) session.getAttribute(LOGIN_USER);
        return userService.findById(dto.getUserId());
    }

    /**
     * 게시글 생성 요청.
     * <p>
     * 로그인한 사용자의 정보 -> 새 게시글을 생성
     * 생성이 완료시, 201 Created 상태 코드 + 생성된 게시글 정보를 반환
     *
     * @param session    현재 로그인된 사용자 정보
     * @param requestDto 게시글 생성에 필요한 내용이 담긴 요청
     * @return 생성된 게시글 정보 DTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FeedResponseDto>> createFeed(HttpSession session,
                                                                   @RequestBody FeedRequestDto requestDto) {
        User loginUser = getLoginUser(session);
        FeedResponseDto feed = feedService.createFeed(loginUser, requestDto);
        String message = feed.getCreator() + "님의 피드가 생성되었습니다.";
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, message, feed));
    }

    /**
     * 피드 전체 조회
     * 1. 로그인한 유저 + 유저의 친구들 : /newsfeeds?page=0&size=10
     * 2. 유저Id -> 친구의 피드 : /newsfeeds?userId=3&page=0&size=10
     *
     * @param userId   /newsfeeds?userId=XX -> 친구 조회
     * @param session  로그인한 유저의 세션 받기
     * @param pageable 페이징 /newsfeeds?page=0&size=10&sort=createdAt,desc
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<FeedResponseDto>>> getFeeds(
            @RequestParam(required = false) Long userId,
            HttpSession session,
            Pageable pageable
    ) {
        // 로그인한 유저+친구의 피드 조회라 로그인 세션이 필요
        User loginUser = getLoginUser(session);
        Page<FeedResponseDto> feeds = feedService.getFeeds(loginUser, userId, pageable);
        String message = "피드 목록 조회 성공!";
        return ResponseEntity.ok(ApiResponse.success(message, feeds));
    }

    /**
     * 단일 피드 조회
     * <p>
     * 피드 ID를 기준으로 특정 피드 조회
     *
     * @param id 조회할 피드 고유 ID
     * @return 조회된 피드 정보 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedResponseDto>> getFeed(@PathVariable Long id) {
        FeedResponseDto feed = feedService.getFeed(id);
        String message = "피드 조회 성공";
        return ResponseEntity.ok(ApiResponse.success(message, feed));
    }


    /**
     * 피드 수정 (작성자만)
     * 로그인유저가 작성한 피드 수정
     *
     * @param id         수정할 피드 고유 ID
     * @param session    현재 로그인된 유저 세션
     * @param requestDto 수정할 내용이 담긴 요청
     * @return 수정된 피드 정보 DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedResponseDto>> updateFeed(@PathVariable Long id,
                                                                   HttpSession session,
                                                                   @RequestBody FeedRequestDto requestDto) {
        User loginUser = getLoginUser(session);
        String message = loginUser.getNickname() + "님의 피드가 수정되었습니다!";
        FeedResponseDto feedResponseDto = feedService.updateFeed(id, loginUser, requestDto);
        return ResponseEntity.ok(ApiResponse.success(message, feedResponseDto));
    }


    /**
     * 피드 삭제 (작성자만)
     * 로그인한 사용자가 작성한 피드만 삭제
     *
     * @param id      삭제할 피드 고유 ID
     * @param session 현재 로그인된 사용자 정보를 담고 있는 세션
     * @return 본문 없이 200 OK 응답
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeed(@PathVariable Long id,
                                                        HttpSession session) {
        User loginUser = getLoginUser(session);
        feedService.deleteFeed(id, loginUser);
        String message = loginUser.getNickname() + "님의 피드가 삭제되었습니다!";
        return ResponseEntity.ok(ApiResponse.success(message));
    }

}