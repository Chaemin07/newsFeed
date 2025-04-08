package com.example.newsfeedproject.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {

  private final Long parentType;
  private final String username;
  private final String comments;
  public CommentRequestDto(Long parentType, String username, String comments){
    this.parentType=parentType;
    this.username=username;
    this.comments=comments;
  }

}
