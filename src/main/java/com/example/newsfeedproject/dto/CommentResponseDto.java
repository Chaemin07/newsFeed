package com.example.newsfeedproject.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {

  private final Long feedId;
  private final Long parentType;
  private final Long likes;
  private final String username;
  private final String comments;
  private final LocalDateTime createdAt;
  private final LocalDateTime modifiedAt;

  public CommentResponseDto(Long feedId, Long parentType, Long likes, String username, String comments,
      LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.feedId = feedId;
    this.parentType = parentType;
    this.likes = likes;
    this.username = username;
    this.comments = comments;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }
}
//RD 다 갈아엎기