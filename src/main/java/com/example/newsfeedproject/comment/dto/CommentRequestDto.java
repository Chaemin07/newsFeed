package com.example.newsfeedproject.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {
  @NotNull
  @Min(0)
  @Max(1)
  private final Long parentType;
  @NotBlank   // merge시 session에서 id를 받아와 user테이블을 검색하는 방향으로...
  private final String username;
  @NotNull
  private final String comments;
  public CommentRequestDto(Long parentType, String username, String comments){
    this.parentType=parentType;
    this.username=username;
    this.comments=comments;
  }

}
