package com.example.climblabs.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.example.climblabs.global.exception.ClimbLabsException;
import com.example.climblabs.global.exception.ExceptionCode;
import com.example.climblabs.global.utils.image.ImageStorageUtils;
import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.domain.ThumbNail;
import com.example.climblabs.post.domain.repository.PostRepository;
import com.example.climblabs.post.web.dto.request.PostFilterRequest;
import com.example.climblabs.post.web.dto.request.PostRequest;
import com.example.climblabs.post.web.dto.response.PostResponse;
import com.example.climblabs.post.web.dto.response.PostScaleTypeResponse;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;
    @Mock
    PostRepository postRepository;
    @Mock
    ImageStorageUtils imageStorageUtils;

    @BeforeEach
    void setUp() {
        given(imageStorageUtils.saveToStorage(any())).willReturn(new ImageFileDto("image", "url"));
        given(postRepository.save(any())).willReturn(new PostDummy(1L));
    }

    private PostRequest createPostRequestDto() {
        return PostRequest
            .builder()
            .title("test")
            .level(1)
            .scale(84)
            .scaleType(ScaleType.BIG)
            .feature("test")
            .city("test")
            .zipCode("1234")
            .street("test")
            .detailStreet("test")
            .sido("test")
            .build();
    }

    @Test
    @DisplayName("게시물 등록에 성공한다.")
    void createPostTest() throws Exception {
        //given
        //when
        long postId = postService.createNewPost(createPostRequestDto());
        //then
        assertThat(postId).isNotNull();

        verify(imageStorageUtils, atLeast(1)).saveToStorage(any());
        verify(postRepository, atLeast(1)).save(any());
    }

    @Test
    @DisplayName("게시물 업데이트에 성공한다.")
    void updatePostTest() throws Exception {
        //given
        Post post = PostDummy.builder()
            .thumbnail(ThumbNail.createThumbNail(new ImageFileDto("test", "url")))
            .build();
        post.setImages(Lists.newArrayList(new ImageFileDto("test", "url")));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        willDoNothing().given(imageStorageUtils).deleteToImages(any());
        //when
        PostResponse response = postService.updatePost(1L, createPostRequestDto());
        //then
        assertThat(response).isNotNull();
        verify(imageStorageUtils).saveToStorages(any());
        verify(imageStorageUtils).deleteToImages(any());
    }

    @Test
    @DisplayName("게시물 삭제에 성공한다.")
    void deletePostTest() throws Exception{
        //given
        given(postRepository.findById(anyLong()))
            .willReturn(Optional.of(new PostDummy(1L)));
        //when
        postService.deletePost(1L);
        //then
        verify(imageStorageUtils).deleteToImages(any());
        verify(postRepository).delete(any());
    }

    @Test
    @DisplayName("삭제 시 등록된 게시물이 없는 경우 에러 발생 [NOT_FOUND_POST]")
    void deletePostException() throws Exception{
        //given
        //when
        //then
        assertThatThrownBy(() -> postService.deletePost(1L))
            .isInstanceOf(ClimbLabsException.class)
            .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_POST.toString());
    }

    @Test
    @DisplayName("등록된 게시물이 없는 경우 에러 발생 [NOT_FOUND_POST]")
    void updatePostException() throws Exception {
        //given
        //when
        //then
        assertThatThrownBy(() -> postService.updatePost(1L, createPostRequestDto()))
            .isInstanceOf(ClimbLabsException.class)
            .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_POST.toString());
    }

    @Test
    @DisplayName("암장 이름 검색이 성공한다.")
    void searchTitleTest() throws Exception {
        //given
        given(postRepository.findLikeTitlePosts(anyString(), any()))
            .willReturn(Lists.newArrayList(Post.builder().build()));
        //when
        List<PostResponse> results = postService.searchTitle("클라이밍", Pageable.ofSize(1));
        //then
        assertThat(results).hasSize(1);
        verify(postRepository).findLikeTitlePosts(anyString(), any());
    }

    @Test
    @DisplayName("지역, 암장 규모가 선택되지 않는 경우 필터검색이 성공한다.")
    void searchFilterV1Test() throws Exception {
        //given
        Pageable pageable = Pageable.ofSize(1);
        given(postRepository.findCityPosts(anyString(), any()))
            .willReturn(Lists.newArrayList(Post.builder().build()));
        //when
        PostFilterRequest request = new PostFilterRequest(
            null,
            Lists.newArrayList(ScaleType.ALL)
        );
        List<PostResponse> results = postService.searchFilter("경기", request, pageable);
        //then
        assertThat(results).hasSize(1);
        verify(postRepository).findCityPosts(anyString(), any());
    }

    @Test
    @DisplayName("모든 필터가 선택 된 경우(지역, 암장 규모 ALL) 필터 검색이 성공한다.")
    void searchFilterV2Test() throws Exception {
        //given
        Pageable pageable = Pageable.ofSize(1);
        given(postRepository.findCityAndSidoPosts(anyString(), anyList(), any()))
            .willReturn(Lists.newArrayList(Post.builder().build()));
        //when
        PostFilterRequest request = new PostFilterRequest(
            Lists.newArrayList("강남구"),
            Lists.newArrayList(ScaleType.ALL)
        );
        List<PostResponse> results = postService.searchFilter("경기", request, pageable);
        //then
        assertThat(results).hasSize(1);
        verify(postRepository).findCityAndSidoPosts(anyString(), anyList(), any());
    }

    @Test
    @DisplayName("모든 필터가 선택 된 경우(지역, 암장 규모 All 이 아닐때) 필터 검색이 성공한다.")
    void searchFilterV3Test() throws Exception {
        //given
        Pageable pageable = Pageable.ofSize(1);
        given(postRepository.findCityAndSidoAndScaleTypePosts(anyString(), anyList(), anyList(), any()))
            .willReturn(Lists.newArrayList(Post.builder().build()));
        //when
        PostFilterRequest request = new PostFilterRequest(
            Lists.newArrayList("강남구"),
            Lists.newArrayList(ScaleType.BIG)
        );
        List<PostResponse> results = postService.searchFilter("경기", request, pageable);
        //then
        assertThat(results).hasSize(1);
        verify(postRepository).findCityAndSidoAndScaleTypePosts(anyString(), anyList(), anyList(), any());
    }

    @Test
    @DisplayName("지역 필터만 선택된 경우 필터검색이 성공한다.")
    void searchFilterV4Test() throws Exception {
        //given
        Pageable pageable = Pageable.ofSize(1);
        given(postRepository.findCityAndSidoPosts(anyString(), anyList(), any()))
            .willReturn(Lists.newArrayList(Post.builder().build()));
        //when
        PostFilterRequest request = new PostFilterRequest(
            Lists.newArrayList("강남구"),
            null
        );
        List<PostResponse> results = postService.searchFilter("경기", request, pageable);
        //then
        assertThat(results).hasSize(1);
        verify(postRepository).findCityAndSidoPosts(anyString(), anyList(), any());
    }

    @Test
    @DisplayName("암장 규모만 선택되어있을 경우 필터검색이 성공한다.")
    void searchFilterV5Test() throws Exception {
        //given
        Pageable pageable = Pageable.ofSize(1);
        given(postRepository.findCityAndScaleTypePosts(anyString(), anyList(), any()))
            .willReturn(Lists.newArrayList(Post.builder().build()));
        //when
        PostFilterRequest request = new PostFilterRequest(
            null,
            Lists.newArrayList(ScaleType.BIG)
        );
        List<PostResponse> results = postService.searchFilter("경기", request, pageable);
        //then
        assertThat(results).hasSize(1);
        verify(postRepository).findCityAndScaleTypePosts(anyString(), anyList(), any());
    }

    @Test
    @DisplayName("암장 규모에 따른 랜덤 검색이 성공한다.")
    void readFilterScaleTypeTest() throws Exception {
        //given
        given(postRepository.findByRandomScaleTypeLimit(anyString(), anyInt()))
            .willReturn(Lists.newArrayList(PostDummy.builder().scaleType(ScaleType.BIG).build()));
        //when
        Optional<List<ScaleType>> scaleTypes = Optional.of(Lists.newArrayList(ScaleType.BIG, ScaleType.MIDDLE));
        PostScaleTypeResponse result = postService.readFilterScaleType(1, scaleTypes);
        //then
        assertThat(result).isNotNull();
        verify(postRepository, atLeast(2)).findByRandomScaleTypeLimit(anyString(), anyInt());
    }

    @Test
    @DisplayName("2개 게시물 랜덤 검색이 성공한다.")
    void readRandomPostTest() throws Exception {
        //given
        given(postRepository.findByRandomLimitPost(anyInt()))
            .willReturn(Lists.newArrayList(Post.builder().build(), Post.builder().build()));

        //when
        List<PostResponse> results = postService.readRandomPost(2);
        //then
        assertThat(results).hasSize(2);
        verify(postRepository).findByRandomLimitPost(anyInt());
    }

}