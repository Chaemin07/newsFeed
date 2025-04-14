package com.example.newsfeedproject.comment.controller;

import static com.example.newsfeedproject.auth.SessionManager.LOGIN_USER;

import com.example.newsfeedproject.auth.dto.LoginResponseDto;
import com.example.newsfeedproject.comment.dto.CommentRequestDto;
import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.dto.CommentUpdateRequestDto;
import com.example.newsfeedproject.comment.service.CommentService;

import com.example.newsfeedproject.common.response.ApiResponse;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotNull;


@RestController
@RequestMapping("/newsfeeds/comment")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;


    @PostMapping("/{parentId}")//생성
    public ResponseEntity<ApiResponse<CommentResponseDto>> save(@PathVariable @NotNull @Min(1) Long parentId, @Valid @RequestBody CommentRequestDto requestDto,
                                                                HttpSession session) {
        LoginResponseDto user = (LoginResponseDto) session.getAttribute(LOGIN_USER);
        long userId = user.getUserId();
        User loginUser = userService.findById(userId);
        CommentResponseDto commentResponseDto = commentService.save(requestDto, user.getUserName(), loginUser, parentId);
        String message = "댓글이 생성되었습니다.";
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, message, commentResponseDto));
    }

    @GetMapping(value = "/comments")//전체조회-댓글,답글
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> findAllByParentIdAndParentType(@RequestParam @NotNull @Min(1) Long parentId, @RequestParam @NotNull Long parentType) {
        List<CommentResponseDto> commentResponseDtoList = commentService.findAllByParentIdAndParentType(parentId, parentType);
        String message = "댓글 목록을 조회했습니다.";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, message, commentResponseDtoList));
    }

    @GetMapping("/{commentId}")//단일조회, 답글 전체보기용, 삭제해도 무관함
    public ResponseEntity<ApiResponse<CommentResponseDto>> findByCommentId(@PathVariable @NotNull @Min(1) Long commentId) {
        CommentResponseDto commentResponseDto = commentService.findByCommentId(commentId);
        String message = "댓글 목록을 조회했습니다.";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, message, commentResponseDto));
    }

    @PatchMapping("/{commentId}")//수정
    public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable @NotNull @Min(1) Long commentId, @Valid @RequestBody CommentUpdateRequestDto requestDto, HttpSession session) {
        LoginResponseDto user = (LoginResponseDto) session.getAttribute(LOGIN_USER);
        Long userId = user.getUserId();
        commentService.updateComment(userId, commentId, requestDto.getContents());
        // 응답 메세지는 추후 수정하셔도 됩니다!
        String message = "댓글이 수정되었습니다.";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(message));
    }

    @PatchMapping("/comments/{commentId}")// 삭제 전 무효화처리, 글만 삭제시킴
    public ResponseEntity<ApiResponse<Void>> disableComment(@PathVariable @NotNull @Min(1) Long commentId, HttpSession session) {
        String Disabler = "삭제된 글입니다.";
        LoginResponseDto user = (LoginResponseDto) session.getAttribute(LOGIN_USER);
        Long userId = user.getUserId();
        commentService.updateComment(userId, commentId, Disabler);
        // 응답 메세지는 추후 수정하셔도 됩니다!
        String message = "댓글이 비활성화되었습니다.";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(message));
    }

    @DeleteMapping("/{commentId}")//완전 삭제용
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable @NotNull @Min(1) Long commentId, HttpSession session) {
        LoginResponseDto user = (LoginResponseDto) session.getAttribute(LOGIN_USER);
        Long userId = user.getUserId();
        commentService.deleteComment(userId, commentId);
        // 응답 메세지는 추후 수정하셔도 됩니다!
        String message = "댓글이 삭제되었습니다.";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(message));
    }
}