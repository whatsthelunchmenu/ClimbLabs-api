package com.example.climblabs.post.web.dto.response;

import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.post.domain.Address;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.domain.ThumbNail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class PostResponse {

    private Long id;

    private String title;

    private int level;

    private String city;

    private String zipCode;

    private String street;

    private String detailStreet;

    private String sido;

    private Integer scale;

    private ScaleType scaleType;

    private String feature;

    private String thumbNailUrl;

    private List<String> advantages;

    private List<String> disAdvantages;

    private List<String> images;

    @Builder
    public PostResponse(Long id,
                           String title,
                           int level,
                           String city,
                           String zipCode,
                           String street,
                           String detailStreet,
                           String sido,
                           Integer scale,
                           ScaleType scaleType,
                           String feature,
                           String thumbNailUrl,
                           List<String> advantages,
                           List<String> disAdvantages,
                           List<String> images) {
        this.id = id;
        this.title = title;
        this.level = level;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.detailStreet = detailStreet;
        this.sido = sido;
        this.scale = scale;
        this.scaleType = scaleType;
        this.feature = feature;
        this.thumbNailUrl = thumbNailUrl;
        this.advantages = advantages;
        this.disAdvantages = disAdvantages;
        this.images = images;
    }

    public static PostResponse of(Post it) {

        List<String> advantageList = it.getAdvantageItems();
        List<String> disAdvantiageList = it.getDisAdvantageItems();
        List<String> imageList = it.getImagePaths();
        Address address = Optional.ofNullable(it.getAddress()).orElse(new Address());
        ImageFileDto imageFileDto = ImageFileDto.builder().build();
        ThumbNail thumbNail = Optional.ofNullable(it.getThumbnail()).orElse(ThumbNail.createThumbNail(imageFileDto));

        return PostResponse.builder()
                .id(it.getId())
                .title(it.getTitle())
                .level(it.getLevel())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .street(address.getStreet())
                .detailStreet(address.getDetailStreet())
                .sido(address.getSido())
                .scale(it.getScale())
                .scaleType(it.getScaleType())
                .feature(it.getFeature())
                .thumbNailUrl(thumbNail.getThumbNailUrl())
                .advantages(advantageList)
                .disAdvantages(disAdvantiageList)
                .images(imageList)
                .build();
    }
}
