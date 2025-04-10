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
     * 게시글 생성
     */
    public ResponseEntity<FeedResponseDto> createFeed(HttpSession session,
                                                      @RequestBody FeedRequestDto requestDto) {
        User loginUser = getLoginUser(session);
        return ResponseEntity.status(201).body(feedService.createFeed(loginUser, requestDto));
    }

    /**
     * 게시글 전체 조회 (페이징 + 정렬)
     */
    @GetMapping
    public ResponseEntity<Page<FeedResponseDto>> getAllFeeds(Pageable pageable) {
        return ResponseEntity.ok(feedService.getAllFeeds(pageable));
    }

    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedResponseDto> getFeed(@PathVariable Long id) {
        return ResponseEntity.ok(feedService.getFeed(id));
    }


    /**
     * 게시글 수정 (작성자 본인만 가능)
     * @SessionAttribute로 로그인 정보 확인 -> controller에서 예외처리로 게시글 작성자만 가능하도록(삭제도 동일)
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedResponseDto> updateFeed(@PathVariable Long id,
                                                      HttpSession session,
                                                      @RequestBody FeedRequestDto requestDto) {
        User loginUser = getLoginUser(session);
        return ResponseEntity.ok(feedService.updateFeed(id, loginUser, requestDto));
    }

    /**
     * 게시글 삭제 (작성자 본인만 가능)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeed(@PathVariable Long id,
                                           HttpSession session) {
        User loginUser = getLoginUser(session);
        feedService.deleteFeed(id, loginUser);

        // 200OK 상태 코드로만 응답, 본문은 없게
        return ResponseEntity.ok().build();
    }
}