package com.example.newsfeedproject.like.entity.followentity;

import com.example.newsfeedproject.user.entity.User;
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
