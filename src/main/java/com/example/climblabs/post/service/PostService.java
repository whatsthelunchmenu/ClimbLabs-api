package com.example.climblabs.post.service;

import com.example.climblabs.global.utils.image.ImageStorageUtils;
import com.example.climblabs.post.domain.Image.Image;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import com.example.climblabs.post.domain.repository.PostRepository;
import com.example.climblabs.post.web.dto.PostRequest;
import com.example.climblabs.post.web.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    private final ImageStorageUtils imageStorageUtils;


    @Transactional
    public long createNewPost(PostRequest request) {
        Post newPost = postRepository.save(createPost(request));
        return newPost.getId();
    }

    private Post createPost(PostRequest request) {

        Set<Image> images = request.convertImages(
                imageStorageUtils.saveToStorage(request.getImages()));
        Set<Advantage> advantages = request.convertAdvantage(request.getAdvantages());
        Set<DisAdvantage> disAdvantages = request.convertDisAdvantage(request.getDisAdvantages());

        return request.getPost(images, advantages, disAdvantages);
    }

    public List<PostResponse> readPost(Pageable pageable) {
        List<Post> posts = postRepository.findAllByPosts(pageable);
        return posts.stream()
                .map(it -> PostResponse.of(it))
                .collect(Collectors.toList());
    }
}
