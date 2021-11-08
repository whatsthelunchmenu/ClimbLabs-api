package com.example.climblabs.post.web;


import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.PostRequest;
import com.example.climblabs.post.web.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<?> create(PostRequest request) {
        long postId = postService.createNewPost(request);

        return ResponseEntity.ok().body(PostResponse.builder()
            .postId(postId)
            .build());
    }

}
