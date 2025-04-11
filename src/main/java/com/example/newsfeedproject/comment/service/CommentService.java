package com.example.newsfeedproject.comment.service;


import com.example.newsfeedproject.comment.dto.CommentRequestDto;
import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.comment.repository.CommentRepository;

import com.example.newsfeedproject.feed.entity.NewsFeed;
import com.example.newsfeedproject.feed.repository.NewsFeedRepository;
import com.example.newsfeedproject.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
  private final CommentRepository commentRepository;
  private final NewsFeedRepository newsFeedRepository;

  public CommentResponseDto save(CommentRequestDto requestDto, String userName, User userId,
      Long parentId) {//저장
    Long target;
    NewsFeed owner;
    if(requestDto.getParentType()==1){
      Optional<Comment> optionalParentComment = Optional.ofNullable(
          commentRepository.findByCommentId(parentId)
              .orElseThrow(() -> new CustomException(ErrorCode.DOES_NOT_EXIST)));
      Comment parentComment = optionalParentComment.get();
      target = parentComment.getParentId();

      Optional<NewsFeed> optionalOwner = Optional.ofNullable(newsFeedRepository.findById(target)
          .orElseThrow(() -> new CustomException(ErrorCode.DOES_NOT_EXIST)));
      owner = optionalOwner.get();

    } else if (requestDto.getParentType()==0){
      Optional<NewsFeed> optionalOwner = Optional.ofNullable(newsFeedRepository.findById(parentId)
          .orElseThrow(() -> new CustomException(ErrorCode.DOES_NOT_EXIST)));
      owner = optionalOwner.get();

    } else {throw new CustomException(ErrorCode.WRONG_PARENT_TYPE);    }
    Comment comment = new Comment(parentId, requestDto.getParentType(), owner, userName, userId,
        requestDto.getContents(), "active");
    log.info("inside if checks : {},{},{},{}",owner,comment,userName,userId );
    commentRepository.save(comment);
    return new CommentResponseDto(
        comment.getParentId(),
        comment.getParentType(),
        comment.getUsername(),
        comment.getContents(),
        comment.getCreatedAt(),
        comment.getModifiedAt()
    );
  }

  public CommentResponseDto findByCommentId(Long commentId) {//단일 댓글 조회
    Optional<Comment> optionalComment = commentRepository.findByCommentId(commentId);
    if(optionalComment.isEmpty()){throw new CustomException(ErrorCode.DOES_NOT_EXIST);}
    Comment findComment = optionalComment.get();
    return new CommentResponseDto(
        findComment.getParentId(),
        findComment.getParentType(),
        findComment.getUsername(),
        findComment.getContents(),
        findComment.getCreatedAt(),
        findComment.getModifiedAt());
  }

  public void updateComment(Long userid, Long commentId, String contents) {//업데이트기
    Comment findComment = commentRepository.findByCommentIdOrElseThrow(commentId);
    StringBuilder status=new StringBuilder();
    if (!userid.equals(findComment.getUserid().getId())) {
      if (!userid.equals(findComment.getOwner().getCreator().getId())) {
        throw new CustomException(ErrorCode.ACCESS_DENIED);
      }
    }
    if (!Objects.equals(findComment.getStatus(), "active")) {
      throw new CustomException(ErrorCode.DOES_NOT_EXIST);
    }
    if (contents.equals("삭제된 글입니다.")){
      status.append("disabled");
    } else status.append("active");
    findComment.UpdateComment(commentId, contents, status.toString());
    commentRepository.save(findComment);
}

  public void deleteComment(Long commentId) {//완전삭제
    Comment findComment = commentRepository.findByCommentIdOrElseThrow(commentId);
    if (Objects.equals(findComment.getStatus(), "active")) {
      throw new CustomException(ErrorCode.PREEMPTIVE_ACTION_REQUIRED);
    } else if (Objects.equals(findComment.getStatus(), "disabled")) {
      commentRepository.delete(findComment);
    } else {
      throw new CustomException(ErrorCode.UNAUTHORIZED_DATA_MANUPILATION_FOUND);
    }
  }

  public List<CommentResponseDto> findAllByParentIdAndParentType(Long parentId,Long parentType) {//댓글 or 답글 전체 조회
    if(parentType==0||parentType==1) {
      List<Comment> commentlist = commentRepository.findAllByParentIdAndParentType(parentId, parentType);
        return  commentlist.stream().map(CommentResponseDto::toDto).toList();
    }else{
      log.warn("불순한 조작값 입력 감지");
      return new ArrayList<>();
    }
  }
}
