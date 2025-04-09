package com.example.newsfeedproject.service;


import com.example.newsfeedproject.dto.CommentResponseDto;
import com.example.newsfeedproject.entity.Comment;
import com.example.newsfeedproject.handler.MismatchException;
import com.example.newsfeedproject.repository.CommentRepository;
import com.example.newsfeedproject.repository.LikesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
  private final CommentRepository commentRepository;
  private final LikesRepository likesRepository;
  //좋아요 저장소 연결용(예시코드이므로 수정필요)
  //LikesRepository likesrepository;
  public CommentResponseDto save(Long parentId, Long parentType, String username, String contents) {//저장
    Comment comment = new Comment(parentId, parentType, 0L, username, contents, 0L);
    commentRepository.save(comment);
    return new CommentResponseDto(
        comment.getParentId(),
        comment.getParentType(),
        comment.getLikes(),
        comment.getUsername(),
        comment.getContents(),
        comment.getCreatedAt(),
        comment.getModifiedAt());
  }

//좋아요&답글 수 갱신기. 예시코드이므로 오류 가능성 있음.
  public void updateSubs(Long commentId,Long parentType){
  try{
    Long likes = likesRepository.countByCommentIdAndParentType(commentId, parentType);
    Comment findComment = commentRepository.findByCommentIdOrElseThrow(commentId);
    Long answers = commentRepository.findByCommentIdAndParentType(commentId, parentType);
    findComment.UpdateSubs(likes, answers);

    }catch(Exception e){log.error("Exception error: 갱신기 오류발생!!");}
  }



  public CommentResponseDto findByCommentId(Long commentId) {//단일 댓글 조회, 하나의 댓글을 자세히 보기용+페이징 추가시 limit 무시하고 답글 로드
    Optional<Comment> optionalComment = commentRepository.findByCommentId(commentId);
    if(optionalComment.isEmpty()){throw new MismatchException(HttpStatus.NOT_FOUND, "해당 글이 없습니다 : "+commentId);}
    Comment findComment = optionalComment.get();
    return new CommentResponseDto(
        findComment.getParentId(),
        findComment.getParentType(),
        findComment.getLikes(),
        findComment.getUsername(),
        findComment.getContents(),
        findComment.getCreatedAt(),
        findComment.getModifiedAt());
  }

  public void updateComment(Long commentId, String contents) {
    Comment findSchedule = commentRepository.findByCommentIdOrElseThrow(commentId);
    findSchedule.UpdateComment(commentId, contents);
  }

  public void deleteComment(Long commentId) {
    Comment findSchedule = commentRepository.findByCommentIdOrElseThrow(commentId);
    commentRepository.delete(findSchedule);
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
