package com.example.climblabs.post.service;

import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.domain.ThumbNail;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDummy extends Post {

    private Long id;

    private ScaleType scaleType;

    private ThumbNail thumbnail;

    public PostDummy(Long id) {
        this.id = id;
    }

    @Override
    public List<String> getImageNames() {
        return super.getImageNames();
    }


}