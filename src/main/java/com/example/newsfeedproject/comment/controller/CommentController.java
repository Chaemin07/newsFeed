package com.example.newsfeedproject.comment.controller;

import static com.example.newsfeedproject.auth.SessionManager.LOGIN_USER;

import com.example.newsfeedproject.auth.dto.LoginResponseDto;
import com.example.newsfeedproject.comment.dto.CommentRequestDto;
import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.dto.CommentUpdateRequestDto;
import com.example.newsfeedproject.comment.service.CommentService;

import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/newsfeed/comment")
@RequiredArgsConstructor
@Validated
@Slf4j//테스트용 로그
public class CommentController {

  private final CommentService commentService;
  private final UserService userService;


  @PostMapping("/{parentId}")//생성
  public ResponseEntity<CommentResponseDto> save(@PathVariable @NotNull @Min(1) Long parentId, @Valid @RequestBody CommentRequestDto requestDto,
      HttpSession session) {
    LoginResponseDto user = (LoginResponseDto)session.getAttribute(LOGIN_USER);
    long userId = user.getUserId();
    User loginUser = userService.findById(userId);
    CommentResponseDto commentResponseDto = commentService.save(requestDto, user.getUserName(),loginUser, parentId);
    log.info("controller{},{}",loginUser,requestDto);
    return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
  }

    @GetMapping(value="/comments")//전체조회-댓글,답글
    public ResponseEntity<List<CommentResponseDto>> findAllByParentId(@RequestParam @NotNull @Min(1) Long parentId,@RequestParam @NotNull Long parentType){
      List<CommentResponseDto> commentResponseDtoList = commentService.findAllByParentId(parentId, parentType);
      return new ResponseEntity<>(commentResponseDtoList, HttpStatus.OK);
    }

  @GetMapping("/{commentId}")//단일조회, 답글 전체보기용, 삭제해도 무관함
  public ResponseEntity<CommentResponseDto> findByCommentId(@PathVariable @NotNull @Min(1) Long commentId) {
    commentService.updateSubs(commentId, 0L);
    CommentResponseDto commentResponseDto = commentService.findByCommentId(commentId);
    return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
  }

  @PatchMapping("/{commentId}")//수정
  public ResponseEntity<CommentResponseDto> updateComment(@PathVariable @NotNull @Min(1) Long commentId,@Valid @RequestBody CommentUpdateRequestDto requestDto, HttpSession session) {
    LoginResponseDto user = (LoginResponseDto)session.getAttribute(LOGIN_USER);
    Long userId=user.getUserId();
    commentService.updateComment(userId, commentId, requestDto.getContents());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/comments/{commentId}")// 삭제 전 무효화처리, 글만 삭제시킴
  public ResponseEntity<CommentResponseDto> disableComment(@PathVariable @NotNull @Min(1) Long commentId, HttpSession session) {
    String Disabler="삭제된 글입니다.";
    LoginResponseDto user = (LoginResponseDto)session.getAttribute(LOGIN_USER);
    Long userId=user.getUserId();
    commentService.updateComment(userId,commentId, Disabler);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{commentId}")//완전 삭제용
  public ResponseEntity<Void> deleteComment(@PathVariable @NotNull @Min(1) Long commentId) {
    commentService.deleteComment(commentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}