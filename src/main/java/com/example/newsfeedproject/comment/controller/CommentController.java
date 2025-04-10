package com.example.newsfeedproject.comment.controller;

import com.example.newsfeedproject.comment.dto.CommentRequestDto;
import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.dto.CommentUpdateRequestDto;
import com.example.newsfeedproject.comment.service.CommentService;
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
//@RequestMapping("/newsFeed/comment") //todo: session 에서 userId 받아오기(물어보자)
@RequestMapping("/comment")
@RequiredArgsConstructor
@Validated

public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{parentId}")//생성
  public ResponseEntity<CommentResponseDto> save(@PathVariable @NotNull @Min(1) Long parentId,
      @Valid @RequestBody CommentRequestDto requestDto) {
    CommentResponseDto commentResponseDto = commentService.save(
        parentId,
        requestDto.getParentType(),
        requestDto.getUsername(),
        requestDto.getComments()
    );
    return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
  }

  //테스트용 더미저장소로 로컬 테스트, 좋아요를 받아오기때문에 고장납니다, 재연결 후 수정 필요
    @GetMapping(value="/comments")//전체조회-댓글,답글
    public ResponseEntity<List<CommentResponseDto>> findAllByParentId(@RequestParam @NotNull @Min(1) Long parentId,@RequestParam @NotNull Long parentType){
      commentService.updateSubs(parentId, parentType);
      List<CommentResponseDto> commentResponseDtoList = commentService.findAllByParentId(parentId, parentType);
      return new ResponseEntity<>(commentResponseDtoList, HttpStatus.OK);
    }

  //테스트용 더미저장소로 로컬 테스트, 좋아요를 받아오기때문에 고장납니다, 재연결 후 수정 필요
  @GetMapping("/{commentId}")//단일조회, 페이징 도입 시 페이징 단위를 초과했을경우 답글 전체보기용, 삭제해도 무관함
  public ResponseEntity<CommentResponseDto> findByCommentId(@PathVariable @NotNull @Min(1) Long commentId) {
    commentService.updateSubs(commentId, 0L);
    CommentResponseDto commentResponseDto = commentService.findByCommentId(commentId);
    return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
  }

  //테스트용 더미저장소로 로컬 테스트, 좋아요를 받아오기때문에 고장납니다, 재연결 후 수정 필요
  @PatchMapping("/{commentId}")//수정
  public ResponseEntity<CommentResponseDto> updateComment(@PathVariable @NotNull @Min(1) Long commentId,@Valid @RequestBody CommentUpdateRequestDto requestDto) {
    //commentService.findByCommentId(commentId);
    commentService.updateComment(commentId, requestDto.getContents());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/comments/{commentId}")// 삭제 전 무효화처리, 글만 삭제시킴
  public ResponseEntity<CommentResponseDto> disableComment(@PathVariable @NotNull @Min(1) Long commentId) {
    String Disabler="삭제된 글입니다.";
    commentService.updateComment(commentId, Disabler);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{commentId}")//완전 삭제용
  public ResponseEntity<Void> deleteComment(@PathVariable @NotNull @Min(1) Long commentId) {
    commentService.deleteComment(commentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}