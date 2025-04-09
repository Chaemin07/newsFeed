package com.example.newsfeedproject.like.repository.followrepository;

import com.example.newsfeedproject.like.entity.followentity.Follow;
import com.example.newsfeedproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    Follow findByFollowerAndFollowing(User follower, User following);

    List<Follow> findAllByFollowingId(long followingId);

    List<Follow> findAllByFollowerId(long followerId);
}
