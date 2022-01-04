package com.example.climblabs.post.domain.content;

import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.post.domain.Post;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Builder
    public Image(ImageFileDto imageFileDto){
        this.name = imageFileDto.getName();
        this.url = imageFileDto.getUrl();
    }

    public String getUrl(){
        return url;
    }

    public void setPost(Post post){
        if (this.post != null){
            this.post.getImages().remove(this);
        }
        this.post = post;
        post.getImages().add(this);
    }

    public static Image createImage(ImageFileDto imageFileDto, Post post){
        Image image = new Image();
        image.name = imageFileDto.getName();
        image.url = imageFileDto.getUrl();
        image.post = post;
        return image;
    }
}
