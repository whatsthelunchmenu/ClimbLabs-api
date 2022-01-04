package com.example.climblabs.post.domain;

import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.member.domain.Member;
import com.example.climblabs.post.domain.content.Image;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import com.example.climblabs.post.service.dto.PostDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @Column(name = "POST_ID")
    private Long id;

    private String title;

    private int level;

    @Embedded
    private ThumbNail thumbnail;

    @Embedded
    private Address address;

    private Integer scale;

    @Enumerated(EnumType.STRING)
    private ScaleType scaleType;

    private String feature;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Advantage> advantages = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<DisAdvantage> disAdvantages = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Image> images = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Post(
            String title,
            int level,
            Integer scale,
            Address address,
            ThumbNail thumbnail,
            ScaleType scaleType,
            String feature
    ) {
        this.title = title;
        this.level = level;
        this.address = address;
        this.scale = scale;
        this.scaleType = scaleType;
        this.feature = feature;
        this.thumbnail = thumbnail;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public void setThumbnail(ThumbNail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setMember(Member member) {
        // 기존 연관관계 제거
        if (this.member != null) {
            this.member.getPost().remove(this);
        }
        this.member = member;
        member.getPost().add(this);
    }

    public void setAdvantages(List<String> items) {
        advantages.clear();
        items.stream().forEach(it -> advantages.add(Advantage.createAdvantage(this, it)));
    }

    public void setDisAdvantages(List<String> items) {
        disAdvantages.clear();
        items.stream().forEach(it -> disAdvantages.add(DisAdvantage.createDisAdvantage(this, it)));
    }

    public void setImages(List<ImageFileDto> imageFileDtos) {
        images.clear();

        Set<Image> updateImages = imageFileDtos.stream()
                .map(i -> Image.createImage(i, this))
                .collect(Collectors.toSet());
        updateImages.stream()
                .forEach(it -> images.add(it));
    }

    /*
        비즈니스 로직
     */
    public void update(PostDto postDto) {
        title = postDto.getTitle();
        level = postDto.getLevel();
        address = Address.builder()
                .city(postDto.getCity())
                .zipCode(postDto.getZipCode())
                .detailStreet(postDto.getDetailStreet())
                .street(postDto.getStreet())
                .sido(postDto.getSido())
                .build();
        scale = postDto.getScale();
        scaleType = postDto.getScaleType();
        feature = postDto.getFeature();
        thumbnail = ThumbNail.createThumbNail(postDto.getThumbNailDto());
        setAdvantages(postDto.getAdvantages());
        setDisAdvantages(postDto.getDisAdvantages());
        setImages(postDto.getImageFileDtos());
        updatedAt = LocalDateTime.now();
    }
}
