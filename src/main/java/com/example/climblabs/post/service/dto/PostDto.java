package com.example.climblabs.post.service.dto;

import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.web.dto.request.PostRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private String title;
    private Integer level;
    private String city;
    private String zipCode;
    private String street;
    private String detailStreet;
    private String sido;
    private Integer scale;
    private ScaleType scaleType;
    private String feature;

    private List<ImageFileDto> imageFileDtos;
    private ImageFileDto thumbNailDto;

    private List<String> advantages;
    private List<String> disAdvantages;

    public static PostDto of(PostRequest request, List<ImageFileDto> images, ImageFileDto thumbNail) {
        return PostDto.builder()
                .title(request.getTitle())
                .level(request.getLevel())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .street(request.getStreet())
                .detailStreet(request.getDetailStreet())
                .sido(request.getSido())
                .scale(request.getScale())
                .scaleType(request.getScaleType())
                .feature(request.getFeature())
                .advantages(request.getAdvantages())
                .disAdvantages(request.getDisAdvantages())
                .thumbNailDto(thumbNail)
                .imageFileDtos(images)
                .build();
    }
}
