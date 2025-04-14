package com.example.newsfeedproject.comment.dto;

import com.example.newsfeedproject.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto  {

  private final Long parentId;
  private final Long parentType;
  private final String username;
  private final String contents;
  private final LocalDateTime createdAt;
  private final LocalDateTime modifiedAt;

  public CommentResponseDto(
      Long parentId,
      Long parentType,
      String username,
      String contents,
      LocalDateTime createdAt,
      LocalDateTime modifiedAt) {
    this.parentId = parentId;
    this.parentType = parentType;
    this.username = username;
    this.contents = contents;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

  public static CommentResponseDto toDto(Comment comment) {
    return new CommentResponseDto(
        comment.getParentId(),
        comment.getParentType(),
        comment.getUsername(),
        comment.getContents(),
        comment.getCreatedAt(),
        comment.getModifiedAt());
  }
}
