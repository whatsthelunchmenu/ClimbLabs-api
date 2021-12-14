package com.example.climblabs.post.web.dto.response;

import com.example.climblabs.post.domain.Address;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class PostApiResponse {

    private Long id;

    private String title;

    private String climbingTitle;

    private int level;

    private String city;

    private String zipCode;

    private String street;

    private String detailStreet;

    private Integer scale;

    private ScaleType scaleType;

    private String feature;

    private List<String> advantages;

    private List<String> disAdvantages;

    private List<String> images;

    @Builder
    public PostApiResponse(Long id,
                           String title,
                           String climbingTitle,
                           int level,
                           String city,
                           String zipCode,
                           String street,
                           String detailStreet,
                           Integer scale,
                           ScaleType scaleType,
                           String feature,
                           List<String> advantages,
                           List<String> disAdvantages,
                           List<String> images) {
        this.id = id;
        this.title = title;
        this.climbingTitle = climbingTitle;
        this.level = level;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.detailStreet = detailStreet;
        this.scale = scale;
        this.scaleType = scaleType;
        this.feature = feature;
        this.advantages = advantages;
        this.disAdvantages = disAdvantages;
        this.images = images;
    }

    public static PostApiResponse of(Post it) {

        List<String> advantageList = it.getAdvantageResponseFrom(it.getAdvantages());
        List<String> disAdvantiageList = it.getDisAdvantageResponseFrom(it.getDisAdvantages());
        List<String> imageList = it.getImageResponseFrom(it.getImages());
        Address address = Optional.ofNullable(it.getAddress()).orElse(new Address());
        return PostApiResponse.builder()
                .id(it.getId())
                .title(it.getTitle())
                .climbingTitle(it.getClimbingTitle())
                .level(it.getLevel())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .street(address.getStreet())
                .detailStreet(address.getDetailStreet())
                .scale(it.getScale())
                .scaleType(it.getScaleType())
                .feature(it.getFeature())
                .advantages(advantageList)
                .disAdvantages(disAdvantiageList)
                .images(imageList)
                .build();
    }
}
