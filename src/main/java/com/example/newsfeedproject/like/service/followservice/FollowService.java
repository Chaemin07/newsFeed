package com.example.newsfeedproject.like.service.followservice;

import com.example.newsfeedproject.like.dto.followdto.FollowListResponseDto;
import com.example.newsfeedproject.like.entity.followentity.Follow;
import com.example.newsfeedproject.like.repository.followrepository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    private final UserRepository userRepository;

    public boolean postFollow(long followerId, long followingId) {

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if(follower.getId().equals(followingId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신은 팔로우 할 수 없습니다.");
        }

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if(!followRepository.existsByFollowerAndFollowing(follower, following)) {
            followRepository.save(new Follow(follower, following));
            return true;
        }
        else {
            followRepository.delete(followRepository.findByFollowerAndFollowing(follower, following));
            return false;
        }
    }


    public List<FollowListResponseDto> getFollower(long followingId) {

        List<Follow> followers = followRepository.findAllByFollowingId(followingId);

        return followers.stream().map(f -> new FollowListResponseDto(f.getFollower().getId(), f.getFollower().getName())).toList();

    }

    public List<FollowListResponseDto> getFollowing(long followerId) {

        List<Follow> followings = followRepository.findAllByFollowerId(followerId);

        return followings.stream().map(f -> new FollowListResponseDto(f.getFollowing().getId(), f.getFollowing().getName())).toList();

    }
}
