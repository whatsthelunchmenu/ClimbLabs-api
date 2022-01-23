package com.example.climblabs.post.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostScaleTypeResponse {
    private List<PostResponse> bigs;
    private List<PostResponse> middles;
    private List<PostResponse> smalls;
}
