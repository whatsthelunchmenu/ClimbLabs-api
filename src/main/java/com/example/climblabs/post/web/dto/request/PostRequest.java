package com.example.climblabs.post.web.dto.request;

import com.example.climblabs.admin.web.dto.CommonRequestDto;
import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.post.domain.Address;
import com.example.climblabs.post.domain.ThumbNail;
import com.example.climblabs.post.domain.content.Image;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

@Value
@EqualsAndHashCode(callSuper = true)
@Builder
public class PostRequest extends CommonRequestDto {

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
    private MultipartFile thumbNailImage;
    private List<MultipartFile> images;
    private List<String> advantages;
    private List<String> disAdvantages;

    public Post getPost() {

        return Post.builder()
                .title(this.title)
                .level(this.level)
                .scale(this.scale)
                .scaleType(this.scaleType)
                .feature(this.feature)
                .address(Address.builder()
                        .city(this.city)
                        .zipCode(this.zipCode)
                        .street(this.street)
                        .detailStreet(this.detailStreet)
                        .sido(this.sido)
                        .build())
                .build();
    }
}
