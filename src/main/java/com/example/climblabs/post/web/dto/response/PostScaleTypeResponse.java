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
    private List<PostApiResponse> bigs;
    private List<PostApiResponse> middles;
    private List<PostApiResponse> smalls;
}
