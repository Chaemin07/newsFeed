package com.example.newsfeedproject.service;

import com.example.newsfeedproject.dto.CommentResponseDto;
import com.example.newsfeedproject.entity.Comment;
import com.example.newsfeedproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  public CommentResponseDto save(Long parentId, Long parentType, String username, String contents) {
    Comment comment = new Comment(parentId, parentType, 0L, username, contents);
    commentRepository.save(comment);
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
