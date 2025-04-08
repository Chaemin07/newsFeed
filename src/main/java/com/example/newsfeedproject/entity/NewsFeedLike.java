package com.example.newsfeedproject.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "newsFeedLike")
public class NewsFeedLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    @ManyToOne
    @JoinColumn(name = "newsFeedId")
    private NewsFeed newsFeed;

    public NewsFeedLike(long userId, NewsFeed newsFeed) {
        this.userId = userId;
        this.newsFeed = newsFeed;
    }

    public NewsFeedLike() {
    }
}
