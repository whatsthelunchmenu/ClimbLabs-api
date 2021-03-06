package com.example.climblabs.post.domain.content;

import com.example.climblabs.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Advantage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADVANTAGE_ID")
    private Long id;

    private String item;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Builder
    public Advantage(String item) {
        this.item = item;
    }

    public Advantage(Post post, String item) {
        setPost(post);

        this.item = item;
    }

    public static Advantage createAdvantage(Post post, String item) {
        Advantage advantage = new Advantage();
        advantage.post = post;
        advantage.item = item;
        return advantage;
    }

    public String getItem() {
        return item;
    }

    public void setPost(Post post) {
        //기존에 연결되어있는 연관관계 제거
        if (this.post != null) {
            this.post.getAdvantages().remove(this);
        }
        this.post = post;
        post.getAdvantages().add(this);
    }
}
