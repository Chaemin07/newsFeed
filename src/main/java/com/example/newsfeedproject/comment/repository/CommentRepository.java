package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  void countByParentId(Long parentId);

  default Comment findByCommentIdOrElseThrow(Long commentId){
    return findByCommentId(commentId).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + commentId));
  }

  Optional<Comment> findByCommentId(Long commentId);


  Optional<Comment> findAllByParentIdAndParentType(Long parentId, Long parentType);

  Long countByParentIdAndParentType(Long parentId, Long parentType);

  Comment findByParentIdAndParentType(Long parentId, Long parentType);
}
