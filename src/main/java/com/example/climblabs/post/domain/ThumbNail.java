package com.example.climblabs.post.domain;

import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.post.domain.content.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class ThumbNail {

    private String thumbNailName;

    private String thumbNailUrl;

    public static ThumbNail createThumbNail(ImageFileDto imageFileDto) {
        ThumbNail thumbNail = new ThumbNail();
        thumbNail.thumbNailName = imageFileDto.getName();
        thumbNail.thumbNailUrl = imageFileDto.getUrl();
        return thumbNail;
    }
}
