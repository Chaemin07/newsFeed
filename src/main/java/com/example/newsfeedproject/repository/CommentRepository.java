package com.example.newsfeedproject.repository;

import com.example.newsfeedproject.entity.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  void countByCommentId(Long commentId);

  default Comment findByCommentIdOrElseThrow(Long commentId){
    return findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id =" + commentId));
  }

  Optional<Comment> findByCommentId(Long commentId);


  Optional<Comment> findAllByParentIdAndParentType(Long parentId, Long parentType);

  Long findByCommentIdAndParentType(Long commentId, Long parentType);
}
