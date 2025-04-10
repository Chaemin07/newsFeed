package com.example.newsfeedproject.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {
  @NotNull
  private final String contents;
  public CommentUpdateRequestDto(String contents){
    this.contents=contents;
  }
}
