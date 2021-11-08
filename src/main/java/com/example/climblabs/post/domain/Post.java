package com.example.climblabs.post.domain;

import com.example.climblabs.post.domain.Image.Image;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private String location;

    private String size;

    private String feature;

    @OneToMany(mappedBy = "id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Advantage> advantages = new ArrayList<>();

    @OneToMany(mappedBy = "id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<DisAdvantage> disAdvantages = new ArrayList<>();

    @OneToMany(mappedBy = "id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Image> images = new ArrayList<>();


    @Builder
    public Post(
            String title,
            String climbingTitle,
            int level,
            String location,
            String size,
            String feature,
            List<Advantage> advantages,
            List<DisAdvantage> disAdvantages,
            List<Image> images
                ){
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
}
