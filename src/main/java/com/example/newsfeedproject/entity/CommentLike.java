package com.example.newsfeedproject.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "commentLike")
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    @ManyToOne
    @JoinColumn(name = "commentId")
    private Comment comment;

    public CommentLike(long userId, Comment comment) {
        this.userId = userId;
        this.comment = comment;
    }

    public CommentLike() {
    }
}
