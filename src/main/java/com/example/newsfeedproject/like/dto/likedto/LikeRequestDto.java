package com.example.newsfeedproject.like.dto.likedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDto {

    private long likeId;

    private LikeType likeType;

}
