package com.example.climblabs.post.web.dto;

import com.example.climblabs.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponse {
    private Long id;

    private String title;

    private String climbingTitle;

    private int level;

    private String location;

    private String size;

    private String feature;

    private List<String> advantages;

    private List<String> disAdvantages;

    private List<String> images;

    @Builder
    public PostResponse(Long id,
                        String title,
                        String climbingTitle,
                        int level,
                        String location,
                        String size,
                        String feature, List<String> advantages, List<String> disAdvantages, List<String> images) {
        this.id = id;
        this.title = title;
        this.climbingTitle = climbingTitle;
        this.level = level;
        this.location = location;
        this.size = size;
        this.feature = feature;
        this.advantages = advantages;
        this.disAdvantages = disAdvantages;
        this.images = images;
    }

    public static PostResponse of(Post it) {

        List<String> advantageList = it.getAdvantageResponseFrom(it.getAdvantages());
        List<String> disAdvantiageList = it.getDisAdvantageResponseFrom(it.getDisAdvantages());
        List<String> imageList = it.getImageResponseFrom(it.getImages());

        return PostResponse.builder()
                .id(it.getId())
                .title(it.getTitle())
                .climbingTitle(it.getClimbingTitle())
                .level(it.getLevel())
                .location(it.getLocation())
                .size(it.getSize())
                .feature(it.getFeature())
                .advantages(advantageList)
                .disAdvantages(disAdvantiageList)
                .images(imageList)
                .build();
    }
}
