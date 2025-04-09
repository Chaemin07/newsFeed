package com.example.newsfeedproject.like.dto.likedto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeRequestDto {

    private final long likeId;

    private final LikeType likeType;

}
