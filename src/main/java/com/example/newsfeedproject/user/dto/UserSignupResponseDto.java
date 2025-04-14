package com.example.newsfeedproject.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignupResponseDto {
    private Long userId;
    private String email;
    private String nickname;
}
