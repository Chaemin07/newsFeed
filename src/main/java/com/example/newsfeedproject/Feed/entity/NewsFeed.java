package com.example.newsfeedproject.Feed.entity;

import com.example.newsfeedproject.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
/**
 * @NoArgsConstructor -> 파라미터 없는 기본 생성자 생성(public의 경우, 어디서나 생성 가능함)
 * @NoArgsConstructor(access = AccessLevel.PROTECTED) -> protected, JPA용으로만 제한적으로 사용
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "feed")
public class NewsFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long feedId;

    /**
     * @ManyToOne -> Feed 조회 시 User도 함께 조회
     * (fetch = FetchType.LAZY)를 통해 필요할때만 User 데이터 불러오기 가능
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

//    createdAt / updatedAt @Column은 BaseEntity 또는 팀원 상의 후 정리 예정
//    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

//    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public NewsFeed(User createUser, String contents) {
        this.creator = creator;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 게시글 내용 수정
    public void updateContents(String contents) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}

