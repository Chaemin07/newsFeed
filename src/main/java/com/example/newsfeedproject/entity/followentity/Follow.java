package com.example.newsfeedproject.entity.followentity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_Id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_Id")
    private User following;

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public Follow() {
    }
}
