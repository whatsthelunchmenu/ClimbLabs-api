package com.example.climblabs.post.domain;

import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor
@Getter
@Embeddable
public class ThumbNail {

    private String thumbNailName;

    private String thumbNailUrl;

    public ThumbNail(ImageFileDto imageFileDto) {
        this.thumbNailName = imageFileDto.getName();
        this.thumbNailUrl = imageFileDto.getUrl();
    }
}
