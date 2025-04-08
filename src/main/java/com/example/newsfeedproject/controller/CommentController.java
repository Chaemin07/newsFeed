package com.example.newsfeedproject.controller;

import com.example.newsfeedproject.dto.CommentRequestDto;
import com.example.newsfeedproject.dto.CommentResponseDto;
import com.example.newsfeedproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/newsFeed/comment")
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{feedId}")
  public ResponseEntity<CommentResponseDto> save(@PathVariable Long feedId,@RequestBody CommentRequestDto requestDto){
    CommentResponseDto commentResponseDto = commentService.save(
      feedId,
      requestDto.getParentType(),
      requestDto.getUsername(),
      requestDto.getComments()
    );
return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
  }

}
