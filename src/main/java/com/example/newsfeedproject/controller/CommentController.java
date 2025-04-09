package com.example.newsfeedproject.controller;

import com.example.newsfeedproject.dto.CommentRequestDto;
import com.example.newsfeedproject.dto.CommentResponseDto;
import com.example.newsfeedproject.dto.CommentUpdateRequestDto;
import com.example.newsfeedproject.entity.Comment;
import com.example.newsfeedproject.service.CommentService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/newsFeed/comment") //todo: 1기본테스트 2: 정오 전에 commit 3: session에서 userId받아오기(물어보자)
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{parentId}")//생성
  public ResponseEntity<CommentResponseDto> save(@PathVariable Long parentId,
      @RequestBody CommentRequestDto requestDto) {
    CommentResponseDto commentResponseDto = commentService.save(
        parentId,
        requestDto.getParentType(),
        requestDto.getUsername(),
        requestDto.getComments()
    );
    return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
  }

  //테스트용 더미저장소로 로컬 테스트, merge 후 정리 필요함!!
    @GetMapping(value="/comments")//전체조회-댓글,답글
    public ResponseEntity<List<CommentResponseDto>> findAllByParentId(@RequestParam Long parentId,@RequestParam Long parentType){
      commentService.updateSubs(parentId, parentType);
      List<CommentResponseDto> commentResponseDtoList = commentService.findAllByParentId(parentId, parentType);
      return new ResponseEntity<>(commentResponseDtoList, HttpStatus.OK);
    }

  @GetMapping("/{commentId}")//단일조회, 페이징 도입 시 페이징 단위를 초과했을경우 답글 전체보기용, 삭제해도 무관함
  public ResponseEntity<CommentResponseDto> findByCommentId(@PathVariable Long commentId) {
    commentService.updateSubs(commentId, 0L);
    CommentResponseDto commentResponseDto = commentService.findByCommentId(commentId);
    return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
  }

  @PatchMapping("/{commentId}")//수정
  public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequestDto requestDto) {
    commentService.updateComment(commentId, requestDto.getContents());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/comments/{commentId}")// 삭제 전 무효화처리, 글만 삭제시킴
  public ResponseEntity<CommentResponseDto> disableComment(@PathVariable Long commentId) {
    String Disabler="삭제된 글입니다.";
    commentService.updateComment(commentId, Disabler);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{commentId}")//완전 삭제용
  public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
    commentService.deleteComment(commentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}