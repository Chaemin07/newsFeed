package com.example.newsfeedproject.comment.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;
  @Column
  private Long parentId;//todo :기존과 바뀐점!!
  @Column
  private Long parentType;
  @Column
  private Long likes;
  @Column
  private String username;//todo:기본 테스트 후 user_userid 로 변경
  @Column
  private String contents;
  @Column
  private Long answers;

  public Comment(){
  }

  public Comment(Long parentId, Long parentType, Long likes, String username, String contents, Long answers) {
    this.parentId=parentId;
    this.parentType=parentType;
    this.likes=likes;
    this.username=username;
    this.contents=contents;
    this.answers=answers;
  }

  public void UpdateComment(Long commentId, String contents){
    this.commentId=commentId;
    this.contents=contents;
  }

  public void UpdateSubs(Long likes, Long answers){
    this.likes=likes;
    this.answers=answers;
  }

}
