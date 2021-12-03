package com.example.climblabs.post.web.dto;

import com.example.climblabs.admin.web.dto.CommonRequestDto;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponse extends CommonRequestDto {
    private Long id;

    private String title;

    private String climbingTitle;

    private int level;

    private String zipCode;

    private String address;

    private String detailAddress;

    private ScaleType scaleType;

    private String feature;

    private List<String> advantages;

    private List<String> disAdvantages;

    private List<String> images;

    @Builder
    public PostResponse(Long id,
                        String title,
                        String climbingTitle,
                        int level,
                        String zipCode,
                        String address,
                        String detailAddress,
                        ScaleType scaleType,
                        String feature,
                        List<String> advantages,
                        List<String> disAdvantages,
                        List<String> images) {
        this.id = id;
        this.title = title;
        this.climbingTitle = climbingTitle;
        this.level = level;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.scaleType = scaleType;
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
//                .zipCode(it.getZipCode())
//                .address(it.getAddress())
//                .detailAddress(it.getDetailAddress())
                .scaleType(it.getScaleType())
                .feature(it.getFeature())
                .advantages(advantageList)
                .disAdvantages(disAdvantiageList)
                .images(imageList)
                .build();
    }
}
