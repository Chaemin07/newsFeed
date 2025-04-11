package com.example.newsfeedproject.like.repository.likerepository;

import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.like.entity.likeentity.CommentLike;
import com.example.newsfeedproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByUserAndComment(User user, Comment comment);

    CommentLike findByUserAndComment(User user, Comment comment);

    long countByComment_ParentId(long id);

}
