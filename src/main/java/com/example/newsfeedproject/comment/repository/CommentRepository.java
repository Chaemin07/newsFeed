package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

  default Comment findByCommentIdOrElseThrow(Long commentId){
    return findByCommentId(commentId).orElseThrow(
        () -> new CustomException(ErrorCode.DOES_NOT_EXIST));
  }

  Optional<Comment> findByCommentId(Long commentId);


  List<Comment> findAllByParentIdAndParentType(Long parentId, Long parentType);

}
