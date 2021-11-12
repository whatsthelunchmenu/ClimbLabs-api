package com.example.climblabs.post.domain;

import com.example.climblabs.post.domain.Image.Image;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String climbingTitle;

    private int level;

    private String zipCode;

    private String address;

    private String detailAddress;

    private String size;

    private String feature;

    @OneToMany(mappedBy = "id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Advantage> advantages = new HashSet<>();

    @OneToMany(mappedBy = "id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<DisAdvantage> disAdvantages = new HashSet<>();

    @OneToMany(mappedBy = "id",cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Image> images = new HashSet<>();

    @Builder
    public Post(
            String title,
            String climbingTitle,
            int level,
            String location,
            String size,
            String feature,
            Set<Advantage> advantages,
            Set<DisAdvantage> disAdvantages,
            Set<Image> images
    ) {
        this.title = title;
        this.climbingTitle = climbingTitle;
        this.level = level;
        this.address = location;
        this.size = size;
        this.feature = feature;
        this.advantages = advantages;
        this.disAdvantages = disAdvantages;
        this.images = images;
    }

    public List<String> getAdvantageResponseFrom(Set<Advantage> advantages) {
        return advantages.stream()
                .map(Advantage::getItem)
                .collect(Collectors.toList());
    }

    public List<String> getDisAdvantageResponseFrom(Set<DisAdvantage> disAdvantages) {
        return disAdvantages.stream()
                .map(DisAdvantage::getItem)
                .collect(Collectors.toList());
    }
    public List<String> getImageResponseFrom(Set<Image> images) {
        return images.stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }
}
