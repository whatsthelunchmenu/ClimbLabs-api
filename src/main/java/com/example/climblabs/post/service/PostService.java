package com.example.climblabs.post.service;

import com.example.climblabs.admin.web.dto.CommonRequestDto;
import com.example.climblabs.global.utils.common.SearchType;
import com.example.climblabs.global.utils.image.ImageStorageUtils;
import com.example.climblabs.global.utils.pagination.PaginationInfo;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import com.example.climblabs.post.domain.repository.PostRepository;
import com.example.climblabs.post.web.dto.request.PostRequest;
import com.example.climblabs.post.web.dto.request.PostSearchRequest;
import com.example.climblabs.post.web.dto.response.PostApiResponse;
import com.example.climblabs.post.web.dto.response.PostResponse;
import com.example.climblabs.post.web.dto.response.PostScaleTypeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
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

        Post newPost = request.getPost();
        request.getAdvantages()
                .stream()
                .forEach(item -> new Advantage(newPost, item));

        request.getDisAdvantages()
                .stream()
                .forEach(item -> new DisAdvantage(newPost, item));

        request.convertImages(imageStorageUtils.saveToStorages(request.getImages()))
                .stream().forEach(item -> item.setPost(newPost));

        newPost.setThumbnail(request.convertImage(imageStorageUtils.saveToStorage(request.getThumbNailImage())));
        return newPost;
    }

    public List<PostResponse> readPostApi(Pageable pageable) {
        List<Post> posts = postRepository.findAllByPosts(pageable);
        return posts.stream()
                .map(it -> PostResponse.of(it))
                .collect(Collectors.toList());
    }

    public List<PostResponse> readPost(PostRequest request) {
        List<PostResponse> postResponses = Collections.emptyList();

        int boraderTotalCount = postRepository.findAll().size();
        PaginationInfo paginationInfo = new PaginationInfo(request);
        paginationInfo.setTotalRecordCount(boraderTotalCount);
        request.setPaginationInfo(paginationInfo);

        if (boraderTotalCount > 0) {
            int page = request.getPaginationInfo().getFirstRecordIndex();
            int size = request.getRecordsPerPage();
            List<Post> allByPosts = postRepository.findAllByPosts(PageRequest.of(page, size));
            postResponses = allByPosts.stream()
                    .map(it -> PostResponse.of(it))
                    .collect(Collectors.toList());
        }

        return postResponses;
    }

    public List<PostResponse> findSearchPost(CommonRequestDto request) {

        PaginationInfo paginationInfo = new PaginationInfo(request);
        paginationInfo.setTotalRecordCount(
                getSearchTypePostsTotalCount(request.getSearchType(), request.getSearchValue()));
        request.setPaginationInfo(paginationInfo);

        int page = request.getPaginationInfo().getFirstRecordIndex();
        int size = request.getRecordsPerPage();
        List<Post> posts = getSearchTypePosts(request.getSearchType(), request.getSearchValue(),
                PageRequest.of(page, size));

        return posts.stream()
                .map(it -> PostResponse.of(it))
                .collect(Collectors.toList());
    }

    public List<PostApiResponse> readRandomPost(int limit) {
        List<Post> randomLimitPost = postRepository.findByRandomLimitPost(limit);
        return randomLimitPost.stream()
                .map(PostApiResponse::of)
                .collect(Collectors.toList());
    }

    private int getSearchTypePostsTotalCount(SearchType searchType, String searchValue) {
        long totalCount = 0;
        switch (searchType) {
            case TITLE:
                totalCount = postRepository.countLikeTitlePosts(searchValue);
                break;
        }
        log.info("전체 갯수 : " + totalCount);

        return (int) totalCount;
    }

    private List<Post> getSearchTypePosts(SearchType searchType, String searchValue,
                                          Pageable pageable) {
        List<Post> posts = new ArrayList<>();
        switch (searchType) {
            case TITLE:
                posts = postRepository.findLikeTitlePosts(searchValue, pageable);
                break;
        }
        return posts;
    }

    public PostResponse findByIdPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        return PostResponse.of(post);
    }

    public PostScaleTypeResponse readFilterScaleType(int limit, Optional<List<ScaleType>> scaleTypes) {

        List<ScaleType> types = scaleTypes.orElseGet(getBaseScaleType());
        List<Post> postList = types.stream()
                .map(type -> postRepository.findByRandomScaleTypeLimit(type.toString(), limit))
                .flatMap(it -> it.stream())
                .collect(Collectors.toList());

        List<PostApiResponse> bigTypePosts = getTypePosts(postList, ScaleType.BIG);
        List<PostApiResponse> middleTypePosts = getTypePosts(postList, ScaleType.MIDDLE);
        List<PostApiResponse> smallTypePosts = getTypePosts(postList, ScaleType.SMALL);

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

    private List<PostApiResponse> getTypePosts(List<Post> posts, ScaleType type) {
        return posts.stream()
                .filter(it -> it.getScaleType() == type)
                .map(it -> PostApiResponse.of(it))
                .collect(Collectors.toList());
    }

    public List<PostApiResponse> search(String searchValue, Pageable pageable) {
        log.info(searchValue);
        return postRepository.findLikeTitlePosts(searchValue, pageable)
                .stream()
                .map(it -> PostApiResponse.of(it))
                .collect(Collectors.toList());
    }
}
