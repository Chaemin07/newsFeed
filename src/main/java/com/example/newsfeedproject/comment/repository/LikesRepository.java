package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.comment.entity.likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<likes, Long> {

  Long countByParentIdAndParentType(Long parentId, Long parentType);
}
