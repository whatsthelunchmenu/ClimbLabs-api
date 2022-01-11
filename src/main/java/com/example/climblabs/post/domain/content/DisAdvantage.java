package com.example.climblabs.post.domain.content;

import com.example.climblabs.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DisAdvantage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Builder
    public DisAdvantage(String item) {
        this.item = item;
    }

    public DisAdvantage(Post post, String item) {
        setPost(post);
        this.item = item;
    }

    public static DisAdvantage createDisAdvantage(Post post, String item){
        DisAdvantage disAdvantage = new DisAdvantage();
        disAdvantage.post = post;
        disAdvantage.item = item;
        return disAdvantage;
    }

    public String getItem() {
        return item;
    }

    public void setPost(Post post) {
        if (this.post != null) {
            this.post.getDisAdvantages().remove(this);
        }
        this.post = post;
        post.getDisAdvantages().add(this);
    }

}
