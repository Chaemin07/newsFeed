package com.example.newsfeedproject.like.controller.followcontroller;

import com.example.newsfeedproject.auth.dto.LoginResponseDto;
import com.example.newsfeedproject.common.response.ApiResponse;
import com.example.newsfeedproject.like.dto.followdto.FollowListResponseDto;
import com.example.newsfeedproject.like.dto.followdto.FollowRequestDto;
import com.example.newsfeedproject.like.service.followservice.FollowService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.example.newsfeedproject.auth.SessionManager.LOGIN_USER;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> postFollow(@RequestBody FollowRequestDto request, HttpSession session) {

        LoginResponseDto follower = (LoginResponseDto) session.getAttribute(LOGIN_USER);

        long followerId = follower.getUserId();

        boolean follow = followService.postFollow(followerId, request.getFollowingId());

        if(follow) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("팔로우 완료"));
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("언팔로우 완료"));
        }
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<ApiResponse<List<FollowListResponseDto>>> getFollower(@PathVariable("id") long followingId) {

        List<FollowListResponseDto> followerList = followService.getFollower(followingId);

        String message = "팔로워 목록 조회 성공";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, message,followerList));
    }

    @GetMapping("/{id}/followings")
    public ResponseEntity<ApiResponse<List<FollowListResponseDto>>> getFollowing(@PathVariable("id") long followerId) {

        List<FollowListResponseDto> followingList = followService.getFollowing(followerId);

        String message = "팔로잉 목록 조회 성공";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, message,followingList));
    }
}
