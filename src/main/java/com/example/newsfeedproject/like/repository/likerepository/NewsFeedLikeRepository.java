package com.example.newsfeedproject.like.repository.likerepository;

import com.example.newsfeedproject.feed.entity.NewsFeed;
import com.example.newsfeedproject.like.entity.likeentity.NewsFeedLike;
import com.example.newsfeedproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFeedLikeRepository extends JpaRepository<NewsFeedLike, Long> {
    boolean existsByUserAndNewsFeed(User user, NewsFeed newsFeed);

    NewsFeedLike findByUserAndNewsFeed(User user, NewsFeed newsFeed);

    long countByNewsFeed_FeedId(long id);

}
