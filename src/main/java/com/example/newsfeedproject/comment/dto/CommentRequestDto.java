package com.example.newsfeedproject.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {
  @NotNull
  @Min(0)
  @Max(1)
  private final Long parentType;
  @NotBlank
  private final String contents;
  public CommentRequestDto(Long parentType, String contents){
    this.parentType=parentType;
    this.contents=contents;
  }

}
