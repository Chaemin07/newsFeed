package com.example.newsfeedproject.feed.controller;

import com.example.newsfeedproject.auth.dto.LoginResponseDto;
import com.example.newsfeedproject.feed.dto.request.FeedRequestDto;
import com.example.newsfeedproject.feed.dto.response.FeedResponseDto;
import com.example.newsfeedproject.feed.service.FeedService;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.newsfeedproject.auth.SessionManager.LOGIN_USER;

@RestController
@RequestMapping("/newsfeed")
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
     *
     * 로그인한 사용자의 정보 -> 새 게시글을 생성
     * 생성이 완료시, 201 Created 상태 코드 + 생성된 게시글 정보를 반환
     *
     * @param session 현재 로그인된 사용자 정보
     * @param requestDto 게시글 생성에 필요한 내용이 담긴 요청
     * @return 생성된 게시글 정보 DTO
     */
    @PostMapping
    public ResponseEntity<FeedResponseDto> createFeed(HttpSession session,
                                                      @RequestBody FeedRequestDto requestDto) {
        User loginUser = getLoginUser(session);
        return ResponseEntity.status(201).body(feedService.createFeed(loginUser, requestDto));
    }

    /**
     * 내가 쓴 피드 전체 조회(프로필 눌렀을때 생각하면 됨)
     *
     * @param session
     * @param pageable
     * @return
     */
    @GetMapping("/myprofile")
    public ResponseEntity<Page<FeedResponseDto>> getMyFeeds(
            HttpSession session,
            Pageable pageable
    ) {
        User loginUser = getLoginUser(session);
        return ResponseEntity.ok(feedService.getMyFeeds(loginUser, pageable));
    }

    /**
     * 내가 쓴 피드 + 팔로잉한 사람 피드 조회(인스타 홈 느낌)
     *
     * @param session
     * @param pageable
     * @return
     */
    @GetMapping("/home")
    public ResponseEntity<Page<FeedResponseDto>> getFollowingFeeds(
            HttpSession session,
            Pageable pageable
    ) {
        User loginUser = getLoginUser(session);
        return ResponseEntity.ok(feedService.getFeedsByFollowing(loginUser, pageable));
    }

    /**
     * 단일 피드 조회
     *
     * 피드 ID를 기준으로 특정 피드 조회
     *
     * @param id 조회할 피드 고유 ID
     * @return 조회된 피드 정보 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedResponseDto> getFeed(@PathVariable Long id) {
        return ResponseEntity.ok(feedService.getFeed(id));
    }


    /**
     * 피드 수정 (작성자만)
     * 로그인유저가 작성한 피드 수정
     *
     * @param id 수정할 피드 고유 ID
     * @param session 현재 로그인된 유저 세션
     * @param requestDto 수정할 내용이 담긴 요청
     * @return 수정된 피드 정보 DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedResponseDto> updateFeed(@PathVariable Long id,
                                                      HttpSession session,
                                                      @RequestBody FeedRequestDto requestDto) {
        User loginUser = getLoginUser(session);
        return ResponseEntity.ok(feedService.updateFeed(id, loginUser, requestDto));
    }


    /**
     * 피드 삭제 (작성자만)
     * 로그인한 사용자가 작성한 피드만 삭제
     *
     * @param id 삭제할 피드 고유 ID
     * @param session 현재 로그인된 사용자 정보를 담고 있는 세션
     * @return 본문 없이 200 OK 응답
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeed(@PathVariable Long id,
                                           HttpSession session) {
        User loginUser = getLoginUser(session);
        feedService.deleteFeed(id, loginUser);

        return ResponseEntity.ok().build();
    }

}