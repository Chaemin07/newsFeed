package com.example.newsfeedproject.comment.dto;

import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {

  private final String contents;
  public CommentUpdateRequestDto(String contents){
    this.contents=contents;
  }
}
