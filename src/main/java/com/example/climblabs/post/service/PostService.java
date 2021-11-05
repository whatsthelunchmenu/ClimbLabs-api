package com.example.climblabs.post.service;

import com.example.climblabs.post.domain.Image.Image;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import com.example.climblabs.post.domain.repository.PostRepository;
import com.example.climblabs.post.web.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public long createNewPost(PostRequest request) {
        Post newPost = postRepository.save(createPost(request));
        return newPost.getId();
    }

    private Post createPost(PostRequest request) {
        List<Image> images = request.convertImages(request.getImages());
        List<Advantage> advantages = request.convertAdvantage(request.getAdvantages());
        List<DisAdvantage> disAdvantages = request.convertDisAdvantage(request.getDisAdvantages());

        return request.getPost(images, advantages, disAdvantages);
    }
}
