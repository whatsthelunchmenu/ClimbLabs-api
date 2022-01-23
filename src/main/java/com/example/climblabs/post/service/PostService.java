package com.example.climblabs.post.service;

import com.example.climblabs.global.exception.ClimbLabsException;
import com.example.climblabs.global.exception.ExceptionCode;
import com.example.climblabs.global.utils.image.ImageStorageUtils;
import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.domain.ThumbNail;
import com.example.climblabs.post.domain.repository.PostRepository;
import com.example.climblabs.post.service.dto.PostDto;
import com.example.climblabs.post.web.dto.request.PostFilterRequest;
import com.example.climblabs.post.web.dto.request.PostRequest;
import com.example.climblabs.post.web.dto.response.PostResponse;
import com.example.climblabs.post.web.dto.response.PostScaleTypeResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        Post newPost = request.getPost();

        List<ImageFileDto> imageDtos = imageStorageUtils.saveToStorages(request.getImages());
        ImageFileDto thumbNailDto = imageStorageUtils.saveToStorage(request.getThumbNailImage());

        newPost.update(PostDto.of(request, imageDtos, thumbNailDto));
        return newPost;
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request) {
        Post getPost = postRepository.findById(postId)
            .orElseThrow(() -> new ClimbLabsException(ExceptionCode.NOT_FOUND_POST));

        // 기존에 존재하는 이미지 삭제
        imageStorageUtils.deleteToImages(getPost.getImageNames());
        List<ImageFileDto> imageDtos = imageStorageUtils.saveToStorages(request.getImages());

        ImageFileDto thumbNailDto = ImageFileDto.builder().build();
        ThumbNail thumbnail = getPost.getThumbnail();
        if (thumbnail != null) {
            imageStorageUtils.deleteToImage(thumbnail.getThumbNailName());
            thumbNailDto = imageStorageUtils.saveToStorage(request.getThumbNailImage());
        }

        getPost.update(PostDto.of(request, imageDtos, thumbNailDto));

        return PostResponse.of(getPost);
    }

    public List<PostResponse> readRandomPost(int limit) {
        List<Post> randomLimitPost = postRepository.findByRandomLimitPost(limit);
        return randomLimitPost.stream()
            .map(PostResponse::of)
            .collect(Collectors.toList());
    }

    public PostScaleTypeResponse readFilterScaleType(int limit, Optional<List<ScaleType>> scaleTypes) {

        List<ScaleType> types = scaleTypes.orElseGet(getBaseScaleType());
        List<Post> postList = types.stream()
            .map(type -> postRepository.findByRandomScaleTypeLimit(type.toString(), limit))
            .flatMap(it -> it.stream())
            .collect(Collectors.toList());

        List<PostResponse> bigTypePosts = getTypePosts(postList, ScaleType.BIG);
        List<PostResponse> middleTypePosts = getTypePosts(postList, ScaleType.MIDDLE);
        List<PostResponse> smallTypePosts = getTypePosts(postList, ScaleType.SMALL);

        return new PostScaleTypeResponse(bigTypePosts, middleTypePosts, smallTypePosts);
    }

    private Supplier<List<ScaleType>> getBaseScaleType() {
        return () -> {
            List<ScaleType> types = new ArrayList<>();
            types.add(ScaleType.BIG);
            types.add(ScaleType.MIDDLE);
            return types;
        };
    }

    private List<PostResponse> getTypePosts(List<Post> posts, ScaleType type) {
        return posts.stream()
            .filter(it -> it.getScaleType() == type)
            .map(it -> PostResponse.of(it))
            .collect(Collectors.toList());
    }

    public List<PostResponse> searchTitle(String searchValue, Pageable pageable) {
        return postRepository.findLikeTitlePosts(searchValue, pageable)
            .stream()
            .map(it -> PostResponse.of(it))
            .collect(Collectors.toList());
    }

    public List<PostResponse> searchFilter(String city, PostFilterRequest request, Pageable pageable) {

        // 모든 필터가 선택되지 않은 경우
        if (ObjectUtils.isEmpty(request.getSidos()) && request.getScaleTypes().contains(ScaleType.ALL)) {
            return getPostInCityFilter(city, pageable);
        }// 모든 필터가 선택된 경우
        else if (!ObjectUtils.isEmpty(request.getSidos()) && !ObjectUtils.isEmpty(request.getScaleTypes())) {
            if (request.getScaleTypes().contains(ScaleType.ALL)) {
                return getPostInCityAndSidoFilter(city, request, pageable);
            } else {
                return getPostInCityAndSidoAndScaleTypeFilter(city, request, pageable);
            }
        }// 하나만 선택된 경우
        else {
            //sido가 비어있을 경우
            if (ObjectUtils.isEmpty(request.getSidos())) {
                return getPostInCityAndScaleTypeFilter(city, request, pageable);
            }
            // scaleType이 비어있을 경우
            else {
                return getPostInCityAndSidoFilter(city, request, pageable);
            }
        }
    }

    private List<PostResponse> getPostInCityAndSidoAndScaleTypeFilter(String city, PostFilterRequest request, Pageable pageable) {
        return postRepository.findCityAndSidoAndScaleTypePosts(city, request.getSidos(), request.getScaleTypes(), pageable)
            .stream()
            .map(it -> PostResponse.of(it))
            .collect(Collectors.toList());
    }

    private List<PostResponse> getPostInCityFilter(String city, Pageable pageable) {
        return postRepository.findCityPosts(city, pageable)
            .stream()
            .map(it -> PostResponse.of(it))
            .collect(Collectors.toList());
    }

    private List<PostResponse> getPostInCityAndSidoFilter(String city, PostFilterRequest request, Pageable pageable) {
        return postRepository.findCityAndSidoPosts(city, request.getSidos(), pageable)
            .stream()
            .map(it -> PostResponse.of(it))
            .collect(Collectors.toList());
    }

    private List<PostResponse> getPostInCityAndScaleTypeFilter(String city, PostFilterRequest request, Pageable pageable) {
        return postRepository.findCityAndScaleTypePosts(city, request.getScaleTypes(), pageable)
            .stream()
            .map(it -> PostResponse.of(it))
            .collect(Collectors.toList());
    }

}
