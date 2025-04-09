package com.example.newsfeedproject.comment.service;


import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.comment.handler.MismatchException;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.comment.repository.LikesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    Comment comment = new Comment(parentId, parentType, 0L, username, contents, 0L,"active");
    commentRepository.save(comment);
    return new CommentResponseDto(
        comment.getParentId(),
        comment.getParentType(),
        comment.getLikes(),
        comment.getUsername(),
        comment.getContents(),
        comment.getCreatedAt(),
        comment.getModifiedAt()
    );
  }

//좋아요&답글 수 갱신기. 예시코드이므로 오류 가능성 있음. merge 전/후로 코드 수정 필요, 더미테스트 통과.
  public void updateSubs(Long parentId,Long parentType){
  try{
    Long likes = likesRepository.countByParentIdAndParentType(parentId, parentType);
    Comment findComment = commentRepository.findByParentIdAndParentType(parentId, parentType);
    if (parentType == 0) {
      Long answers = commentRepository.countByParentIdAndParentType(parentId, 1L);
      findComment.UpdateSubs(likes, answers);
      commentRepository.save(findComment);
    } else if (parentType ==1) {
      Long answers = 0L;
      findComment.UpdateSubs(likes, answers);
      commentRepository.save(findComment);
    } else throw new MismatchException(HttpStatus.BAD_REQUEST,"잘못된 입력값입니다");

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
    Comment findComment = commentRepository.findByCommentIdOrElseThrow(commentId);
    if (!Objects.equals(findComment.getStatus(), "active")) {
      throw new MismatchException(HttpStatus.BAD_REQUEST, "해당 글은 이미 삭제되었습니다.");
    }
    findComment.UpdateComment(commentId, contents);
    commentRepository.save(findComment);
}

  public void deleteComment(Long commentId) {
    Comment findComment = commentRepository.findByCommentIdOrElseThrow(commentId);
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
