package com.example.climblabs.post.domain.Image;

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

    private String url;

    @Builder
    public Image(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
}
