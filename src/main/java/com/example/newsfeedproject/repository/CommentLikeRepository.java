package com.example.newsfeedproject.repository;

import com.example.newsfeedproject.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByUserAndComment(User user, Comment comment);

    CommentLike findByUserAndComment(User user, Comment comment);

    long countByComment_Id(long id);


}
