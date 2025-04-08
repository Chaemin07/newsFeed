package com.example.newsfeedproject.controller;

import com.example.newsfeedproject.dto.LikeRequestDto;
import com.example.newsfeedproject.dto.LikeType;
import com.example.newsfeedproject.service.LikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<String> postLike(@RequestBody LikeRequestDto request, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        likeService.postLike(request, userId);

        return new ResponseEntity<>("좋아요 완료", HttpStatus.OK);

    }

    @GetMapping("/counts")
    public ResponseEntity<String> likeCounter(
            @RequestParam("type") LikeType type,
            @RequestParam("id") long id
    ) {
        long likecount = likeService.likeCounter(type, id);

        return new ResponseEntity<>("좋아요는 총" + likecount + "개 입니다.", HttpStatus.OK);
    }

}
