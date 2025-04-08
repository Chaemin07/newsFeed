package com.example.newsfeedproject.entity;


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
  @Column
  private Long likes;
  @Column
  private String username;
  @Column
  private String contents;

  public Comment(){
  }

  public Comment(Long parentId, Long parentType, Long likes, String username, String contents) {
    this.parentId=parentId;
    this.parentType=parentType;
    this.likes=likes;
    this.username=username;
    this.contents=contents;
  }
}
