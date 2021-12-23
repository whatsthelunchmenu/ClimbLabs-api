package com.example.climblabs.post.web.dto.request;

import com.example.climblabs.post.domain.ScaleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostFilterRequest {
    private String sido;
    private ScaleType scaleType;
}
