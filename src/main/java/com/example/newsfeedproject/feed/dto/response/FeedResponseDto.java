package com.example.newsfeedproject.feed.dto.response;

/**
 * 게시글 응답시 클라이언트에게 전달할 데이터
 */

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FeedResponseDto {
    private Long feedId;
    private String creator;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    /**
     * FeedRequest에서 작성한 것과 동일(내용이 단순해서 update를 분리하지 않음)
     */

    /**
     * 단건 조회 할때 댓글 수, 좋아요 수 추가하고 싶은데 상의 해보기
     * private Long commentCount;
     * private Long likeCount;
     */

}