package com.example.newsfeedproject.like.service.likeservice;

import com.example.newsfeedproject.like.dto.likedto.LikeRequestDto;
import com.example.newsfeedproject.like.dto.likedto.LikeType;
import com.example.newsfeedproject.like.entity.likeentity.CommentLike;
import com.example.newsfeedproject.like.entity.likeentity.NewsFeedLike;
import com.example.newsfeedproject.like.repository.likerepository.CommentLikeRepository;
import com.example.newsfeedproject.like.repository.likerepository.NewsFeedLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final CommentLikeRepository commentLikeRepository;

    private final NewsFeedLikeRepository newsFeedLikeRepository;

    private final NewsFeedRepository newsFeedRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    public void postLike(LikeRequestDto request, long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        validateAuthor(request.getLikeType(), request.getLikeId(), user.getId());

        switch(request.getLikeType()) {
            case NEWSFEED :
                // 404 DB 내 데이터 존재 x
                NewsFeed newsFeed = newsFeedRepository.findById(request.getLikeId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
                if(!newsFeedLikeRepository.existsByUserAndNewsFeed(user, newsFeed)) {
                    NewsFeedLike newsFeedLike = new NewsFeedLike(user, newsFeed);
                    newsFeedLikeRepository.save(newsFeedLike);
                }
                else {
                    NewsFeedLike newsFeedLike = newsFeedLikeRepository.findByUserAndNewsFeed(user, newsFeed);
                    newsFeedLikeRepository.delete(newsFeedLike);
                }
                break;

            case COMMENT :
                // 404 DB 내 데이터 존재 x
                Comment comment = commentRepository.findById(request.getLikeId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));
                if(!commentLikeRepository.existsByUserAndComment(user, comment)) {
                    CommentLike commentLike = new CommentLike(user, comment);
                    commentLikeRepository.save(commentLike);
                }
                else {
                    CommentLike commentLike = commentLikeRepository.findByUserAndComment(user, comment);
                    commentLikeRepository.delete(commentLike);
                }
                break;
        }
    }

    public long likeCounter(LikeType type, long id) {

        switch(type) {
            case NEWSFEED -> {
                return newsFeedLikeRepository.countByNewsFeed_Id(id);
            }
            case COMMENT -> {
                return commentLikeRepository.countByComment_Id(id);
            }
            // 400 잘못된 liketype 입력 시
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 LikeType 입니다.");
        }
    }


    // 본인 작성 확인용
    public void validateAuthor(LikeType type, long likeId, long userId) {
        switch(type) {
            case NEWSFEED:
                // 404 DB 내 데이터 존재 x
                NewsFeed newsFeed = newsFeedRepository.findById(likeId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
                if(newsFeed.getUser().equals(userId)) {
                    // 400 본인이 작성한 게시글에 좋아요를 요청 시
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인 작성글에는 좋아요를 누를 수 없습니다.");
                }
                break;
            case COMMENT:
                // 404 DB 내 데이터 존재 x
                Comment comment = commentRepository.findById(likeId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));
                // 404 DB 내 데이터 존재 x
                NewsFeed checkFeed = newsFeedRepository.findById(comment.getFeed_Id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
                if(checkFeed.getUser().equals(userId)) {
                    // 400 본인이 작성한 댓글에 좋아요를 요청 시
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인 작성글에는 좋아요를 누를 수 없습니다.");
                }
                break;
        }
    }
}
