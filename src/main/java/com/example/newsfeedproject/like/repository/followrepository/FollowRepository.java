package com.example.newsfeedproject.like.repository.followrepository;

import com.example.newsfeedproject.like.entity.followentity.Follow;
import com.example.newsfeedproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    Follow findByFollowerAndFollowing(User follower, User following);

    List<Follow> findAllByFollowingId(long followingId);

    List<Follow> findAllByFollowerId(long followerId);

    /**
     * 내가 팔로우한 사람들의 ID만 추출
     *
     * @param userId > 팔로우 관계의 기준이 되는 사용자 ID (follower)
     * @return 해당 사용자가 팔로우 중인 사용자(following)의 ID 리스트
     */
    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId")
    List<Long> findFollowingIdsByUserId(@Param("userId") Long userId);
}
