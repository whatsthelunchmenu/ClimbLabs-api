package com.example.climblabs.post.web;


import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.PostIdResponse;
import com.example.climblabs.post.web.dto.PostRequest;
import com.example.climblabs.post.web.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        List<PostResponse> postResponses =  postService.readPostApi(pageable);

        return ResponseEntity.ok(postResponses);
    }

}
