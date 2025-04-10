package com.example.newsfeedproject.feed.dto.request;
/**
 * 게시글 생성/수정 시 클라이언트로 전달받을 데이터
 */
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedRequestDto {
    private String content;
    /**
     * contents 외에 나머지는 내용을 안받기 때문에 save와 update를 굳이 분리하지 않음
     */
}
