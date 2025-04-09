package com.example.newsfeedproject.comment.dto;

import com.example.newsfeedproject.comment.entity.Comment;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {

  private final Long parentId;

  private final Long parentType;
  private final Long likes;
  private final String username;
  private final String contents;
  private final LocalDateTime createdAt;
  private final LocalDateTime modifiedAt;

  public CommentResponseDto(
      Long parentId,
      Long parentType,
      Long likes,
      String username,
      String contents,
      LocalDateTime createdAt,
      LocalDateTime modifiedAt) {
    this.parentId = parentId;
    this.parentType = parentType;
    this.likes = likes;
    this.username = username;
    this.contents = contents;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

  public static CommentResponseDto toDto(Comment comment) {
    return new CommentResponseDto(
        comment.getParentId(),
        comment.getParentType(),
        comment.getLikes(),
        comment.getUsername(),
        comment.getContents(),
        comment.getCreatedAt(),
        comment.getModifiedAt());
  }
}
