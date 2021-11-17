package com.example.climblabs.post.web.dto;

import com.example.climblabs.admin.web.dto.CommonRequestDto;
import com.example.climblabs.post.domain.Image.Image;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import lombok.Builder;
import lombok.Value;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class PostRequest extends CommonRequestDto {

    private String title;
    private String climbingTitle;
    private Integer level;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String size;
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

    public Set<Image> convertImages(List<String> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptySet();
        }
        return items.stream().map(Image::new).collect(Collectors.toSet());
    }

    public Post getPost(
            Set<Image> images,
            Set<Advantage> advantages,
            Set<DisAdvantage> disAdvantages
    ) {

        return Post.builder()
                .title(this.title)
                .climbingTitle(this.climbingTitle)
                .level(this.level)
                .zipCode(this.zipCode)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .size(this.size)
                .feature(this.feature)
                .images(images)
                .advantages(advantages)
                .disAdvantages(disAdvantages)
                .build();
    }
}
