package com.example.newsfeedproject.repository.likerepository;

import com.example.newsfeedproject.entity.likeentity.NewsFeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFeedLikeRepository extends JpaRepository<NewsFeedLike, Long> {
    boolean existsByUserAndNewsFeed(User user, NewsFeed newsFeed);

    NewsFeedLike findByUserAndNewsFeed(User user, NewsFeed newsFeed);

    long countByNewsFeed_Id(long id);




}
