package com.example.climblabs.post.web;


import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.request.PostScaleTypeRequest;
import com.example.climblabs.post.web.dto.response.PostIdResponse;
import com.example.climblabs.post.web.dto.request.PostRequest;
import com.example.climblabs.post.web.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<?> create(PostRequest request) {
        long postId = postService.createNewPost(request);
        return ResponseEntity.ok(
                PostIdResponse
                        .builder()
                        .postId(postId)
                        .build()
        );
    }

    @GetMapping("/posts")
    public ResponseEntity<?> readMainPost(@PageableDefault Pageable pageable) {
        List<PostResponse> postResponses = postService.readPostApi(pageable);

        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/posts/random/{limit}")
    public ResponseEntity<?> readRandomPostLimit(@PathVariable int limit) {
        return ResponseEntity.ok(postService.readRandomPost(limit));
    }

    @GetMapping("/posts/random")
    public ResponseEntity<?> readPostFromSacleType(PostScaleTypeRequest request) {
        Optional<List<ScaleType>> scaleTypes = Optional.ofNullable(request.getScaleTypes());
        return  ResponseEntity.ok(postService.readFilterScaleType(request.getLimit(), scaleTypes));
    }

}
