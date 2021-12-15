package com.example.climblabs.post.web.dto.request;

import com.example.climblabs.admin.web.dto.CommonRequestDto;
import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.post.domain.Address;
import com.example.climblabs.post.domain.Image.Image;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import lombok.*;
import org.springframework.util.CollectionUtils;

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
    private Integer scale;
    private ScaleType scaleType;
    private String feature;
    private List<MultipartFile> images;
    private List<String> advantages;
    private List<String> disAdvantages;

    public Set<Advantage> convertAdvantage(List<String> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptySet();
        }
        return items.stream().map(Advantage::new).collect(Collectors.toSet());
    }

    public Set<DisAdvantage> convertDisAdvantage(List<String> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptySet();
        }
        return items.stream().map(DisAdvantage::new).collect(Collectors.toSet());
    }

    public List<Image> convertImages(List<ImageFileDto> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        return items.stream().map(Image::new).collect(Collectors.toList());
    }

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
                        .build())
                .build();
    }
}
