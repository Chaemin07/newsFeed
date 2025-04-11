package com.example.newsfeedproject.comment.service;


import com.example.newsfeedproject.comment.dto.CommentRequestDto;
import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.common.exception.MismatchException;
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
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist parent comment="+parentId)));
      Comment parentComment = optionalParentComment.get();
      target = parentComment.getParentId();

      Optional<NewsFeed> optionalOwner = Optional.ofNullable(newsFeedRepository.findById(target)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
              "Does not exist parentId =" + parentId)));
      owner = optionalOwner.get();
    } else if (requestDto.getParentType()==0){
      Optional<NewsFeed> optionalOwner = Optional.ofNullable(newsFeedRepository.findById(parentId)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
              "Does not exist parentId =" + parentId)));
      owner = optionalOwner.get();
    } else {throw new MismatchException(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다");    }
    Comment comment = new Comment(parentId, requestDto.getParentType(), owner, userName, userId,
        requestDto.getContents(), 0L, "active");

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

  //답글 수 갱신기. 좋아요를 추적하지않음.
  public void updateSubs(Long parentId, Long parentType) {
    Long answers;
    try {
      Comment findComment = commentRepository.findByParentIdAndParentType(parentId, parentType);
      if (parentType == 0) {
        answers = commentRepository.countByParentIdAndParentType(parentId, 1L);
      } else if (parentType == 1) {
        answers = 0L;
      } else {
        throw new MismatchException(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다");
      }

      findComment.UpdateSubs(answers);
      commentRepository.save(findComment);

    } catch (Exception e) {
      log.error("Exception error: 갱신기 오류발생!!");
    }
  }



  public CommentResponseDto findByCommentId(Long commentId) {//단일 댓글 조회
    Optional<Comment> optionalComment = commentRepository.findByCommentId(commentId);
    if(optionalComment.isEmpty()){throw new MismatchException(HttpStatus.NOT_FOUND, "해당 글이 없습니다 : "+commentId);}
    Comment findComment = optionalComment.get();
    return new CommentResponseDto(
        findComment.getParentId(),
        findComment.getParentType(),
        findComment.getUsername(),
        findComment.getContents(),
        findComment.getCreatedAt(),
        findComment.getModifiedAt());
  }

  public void updateComment(Long userid, Long commentId, String contents) {
    Comment findComment = commentRepository.findByCommentIdOrElseThrow(commentId);
    if(!userid.equals(findComment.getUserid().getId())||userid.equals(findComment.getOwner().getCreator().getId())){
      throw new MismatchException(HttpStatus.UNAUTHORIZED,"작성자가 아니면 수정 및 삭제하실 수 없습니다.");
    }
    if (!Objects.equals(findComment.getStatus(), "active")) {
      throw new MismatchException(HttpStatus.UNAUTHORIZED, "해당 글은 이미 삭제되었습니다.");
    }
    findComment.UpdateComment(commentId, contents);
    commentRepository.save(findComment);
}

  public void deleteComment(Long commentId) {
    Comment findComment = commentRepository.findByCommentIdOrElseThrow(commentId);
    if(Objects.equals(findComment.getStatus(), "active")){
      throw new MismatchException(HttpStatus.UNAUTHORIZED,"해당 글은 삭제를 위한 절차를 거치지 않았습니다.");
    }
    commentRepository.delete(findComment);
  }

  public List<CommentResponseDto> findAllByParentId(Long parentId,Long parentType) {//댓글 or 답글 전체 조회
    if(parentType==0||parentType==1) {
      return commentRepository.findAllByParentIdAndParentType(parentId, parentType).stream()
          .map(CommentResponseDto::toDto).toList();
    }else{
      log.warn("불순한 조작값 입력 감지");
      return new ArrayList<>();
    }
  }
}
