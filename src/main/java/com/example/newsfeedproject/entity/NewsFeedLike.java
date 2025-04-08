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

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "newsFeedId")
    private NewsFeed newsFeed;

    public NewsFeedLike(User user, NewsFeed newsFeed) {
        this.user = user;
        this.newsFeed = newsFeed;
    }

    public NewsFeedLike() {
    }
}
