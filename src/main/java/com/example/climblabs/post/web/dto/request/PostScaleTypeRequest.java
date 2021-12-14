package com.example.climblabs.post.web.dto.request;

import com.example.climblabs.post.domain.ScaleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostScaleTypeRequest {

    private List<ScaleType> scaleTypes;
    private int limit;
}
