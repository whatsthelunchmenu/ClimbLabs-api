package com.example.climblabs.post.web.dto;

import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

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

    private ScaleType scaleType;

    private String feature;

    private List<String> advantages;

    private List<String> disAdvantages;

    private List<String> images;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public PostApiResponse(Long id,
        String title,
        String climbingTitle,
        int level,
        String city,
        String zipCode,
        String street,
        String detailStreet,
        ScaleType scaleType,
        String feature,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
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
        this.scaleType = scaleType;
        this.feature = feature;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.advantages = advantages;
        this.disAdvantages = disAdvantages;
        this.images = images;
    }

    public static PostApiResponse of(Post it) {

        List<String> advantageList = it.getAdvantageResponseFrom(it.getAdvantages());
        List<String> disAdvantiageList = it.getDisAdvantageResponseFrom(it.getDisAdvantages());
        List<String> imageList = it.getImageResponseFrom(it.getImages());

        return PostApiResponse.builder()
            .id(it.getId())
            .title(it.getTitle())
            .climbingTitle(it.getClimbingTitle())
            .city(it.getAddress().getCity())
            .zipCode(it.getAddress().getZipCode())
            .street(it.getAddress().getStreet())
            .detailStreet(it.getAddress().getDetailStreet())
            .level(it.getLevel())
            .scaleType(it.getScaleType())
            .feature(it.getFeature())
            .createdAt(it.getCreatedAt())
            .updatedAt(it.getUpdatedAt())
            .advantages(advantageList)
            .disAdvantages(disAdvantiageList)
            .images(imageList)
            .build();
    }
}
