package com.example.climblabs.post.web.dto;

import com.example.climblabs.post.domain.Image.Image;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class PostRequest {

    private String title;
    private String climbingTitle;
    private int level;
    private String location;
    private String size;
    private String feature;
    private List<MultipartFile> images;
    private List<String> advantages;
    private List<String> disAdvantages;

    public List<Advantage> convertAdvantage(List<String> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        return items.stream().map(Advantage::new).collect(Collectors.toList());
    }

    public List<DisAdvantage> convertDisAdvantage(List<String> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        return items.stream().map(DisAdvantage::new).collect(Collectors.toList());
    }

    public List<Image> convertImages(List<String> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        return items.stream().map(Image::new).collect(Collectors.toList());
    }

    public Post getPost(
            List<Image> images,
            List<Advantage> advantages,
            List<DisAdvantage> disAdvantages
    ) {
        return Post.builder()
                .title(this.title)
                .climbingTitle(this.climbingTitle)
                .level(this.level)
                .location(this.location)
                .size(this.size)
                .feature(this.feature)
                .images(images)
                .advantages(advantages)
                .disAdvantages(disAdvantages)
                .build();
    }
}
