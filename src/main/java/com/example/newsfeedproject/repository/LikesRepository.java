package com.example.newsfeedproject.repository;

import com.example.newsfeedproject.entity.likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<likes, Long> {

  Long countByCommentIdAndParentType(Long parentId, Long parentType);
}
