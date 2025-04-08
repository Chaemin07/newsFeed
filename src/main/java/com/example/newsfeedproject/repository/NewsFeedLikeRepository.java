package com.example.newsfeedproject.repository;

import com.example.newsfeedproject.entity.NewsFeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFeedLikeRepository extends JpaRepository<NewsFeedLike, Long> {
    boolean existsByUserIdAndNewsFeed(Long userId, NewsFeed newsFeed);

    NewsFeedLike findByUserIdAndNewsFeed(long userId, NewsFeed newsFeed);

    long countByNewsFeed_Id(long id);
}
