package com.example.newsfeedproject.like.controller.likecontroller;

import com.example.newsfeedproject.auth.dto.LoginResponseDto;
import com.example.newsfeedproject.common.response.ApiResponse;
import com.example.newsfeedproject.like.dto.likedto.LikeRequestDto;
import com.example.newsfeedproject.like.dto.likedto.LikeType;
import com.example.newsfeedproject.like.service.likeservice.LikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static com.example.newsfeedproject.auth.SessionManager.LOGIN_USER;

@Validated
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> postLike(@RequestBody LikeRequestDto request, HttpSession session) {

        LoginResponseDto user = (LoginResponseDto) session.getAttribute(LOGIN_USER);

        long userId = user.getUserId();

        likeService.postLike(request, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("좋아요 완료"));

    }

    @GetMapping("/counts")
    public ResponseEntity<ApiResponse<Void>> likeCounter(
            @RequestParam("type") LikeType type,
            @RequestParam("id") long id
    ) {
        long likecount = likeService.likeCounter(type, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("좋아요는 총 " + likecount + "개 입니다."));
    }

}
