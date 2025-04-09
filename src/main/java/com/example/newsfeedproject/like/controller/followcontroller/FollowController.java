package com.example.newsfeedproject.like.controller.followcontroller;

import com.example.newsfeedproject.like.dto.followdto.FollowListResponseDto;
import com.example.newsfeedproject.like.dto.followdto.FollowRequestDto;
import com.example.newsfeedproject.like.service.followservice.FollowService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<String> postFollow(@RequestBody FollowRequestDto request, HttpSession session) {

        LoginResponseDto follower = (LoginResponseDto) session.getAttribute(LOGIN_USER);

        long followerId = follower.getId();

        boolean follow = followService.postFollow(followerId, request.getFollowingId());

        if(follow) {
            return new ResponseEntity<>("팔로우 완료", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("언팔로우 완료", HttpStatus.OK);
        }
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<FollowListResponseDto>> getFollower(@PathVariable long followingId) {

        List<FollowListResponseDto> followerList = followService.getFollower(followingId);

        return new ResponseEntity<>(followerList, HttpStatus.OK);
    }

    @GetMapping("/{id}/followings")
    public ResponseEntity<List<FollowListResponseDto>> getFollowing(@PathVariable long followerId) {

        List<FollowListResponseDto> followingList = followService.getFollowing(followerId);

        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }
}
