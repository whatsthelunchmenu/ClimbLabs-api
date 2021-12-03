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

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Builder
    public Image(String url){
        this.url = url;
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
}
