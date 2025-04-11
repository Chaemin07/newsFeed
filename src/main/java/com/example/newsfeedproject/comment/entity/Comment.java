package com.example.newsfeedproject.comment.entity;


import com.example.newsfeedproject.feed.entity.NewsFeed;
import com.example.newsfeedproject.user.entity.User;
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
  private Long parentId;
  @Column
  private Long parentType;
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="owner_id")
  private NewsFeed owner;
  @Column
  private String username;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User userid;
  @Column
  private String contents;
  @Column
  private Long answers;
  @Column
  private String status;

  public Comment(){
  }

  public Comment(Long parentId, Long parentType, NewsFeed owner, String username, User userid, String contents, Long answers, String status) {
    this.parentId=parentId;
    this.parentType=parentType;
    this.owner=owner;
    this.username=username;
    this.userid=userid;
    this.contents=contents;
    this.answers=answers;
    this.status=status;
  }

  public void UpdateComment(Long commentId, String contents){
    this.commentId=commentId;
    this.contents=contents;
  }

  public void UpdateSubs(Long answers){
    this.answers=answers;
  }

}
