package com.example.climblabs.post.web.dto.request;

import com.example.climblabs.post.domain.Address;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class PostRequest {


    String title;

    Integer level;

    String city;

    String zipCode;

    String street;

    String detailStreet;

    String sido;

    Integer scale;

    ScaleType scaleType;

    String feature;

    MultipartFile thumbNailImage;

    List<MultipartFile> images;

    List<String> advantages;

    List<String> disAdvantages;

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
