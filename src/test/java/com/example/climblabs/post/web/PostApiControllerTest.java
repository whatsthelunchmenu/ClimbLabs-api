package com.example.climblabs.post.web;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.climblabs.post.domain.Address;
import com.example.climblabs.post.domain.Post;
import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.PostApiResponse;
import com.example.climblabs.post.web.dto.PostRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final String APPLICATION_JSON = "application/json;charset=UTF-8";

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    MultiValueMap<String, String> files;

    @MockBean
    private PostService postService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();

        files = new LinkedMultiValueMap<>();
        files.add("title", "title");
        files.add("climbingTitle", "climbingTitle");
        files.add("level", "1");

        files.add("location", "location");
        files.add("size", "size");
        files.add("feature", "feature");
    }

    @Test
    @DisplayName("게시물 등록에 성공한다.")
    public void createSuccessPost() throws Exception {

        given(postService.createNewPost(any(PostRequest.class))).willReturn(1L);

        //when
        RequestBuilder requestBuilder = multipart("/posts")
            .file("images", "image1".getBytes())
            .file("images", "image2".getBytes())
            .contentType(APPLICATION_JSON)
            .param("advantages", new String[]{"advantage1", "advantage2"})
            .param("disAdvantages", new String[]{"disAdvantage1", "disAdvantage2"})
            .params(files);

        //then
        mockMvc.perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.postId").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("여긴어때 - 4개의 게시물을 무작위로 요청한다.")
    public void test() throws Exception {
        //given
        given(postService.readRandomPost(anyInt())).willReturn(Lists.newArrayList(PostApiResponse.builder()
            .title("test")
            .climbingTitle("test")
            .level(3)
            .city("경기")
            .zipCode("15125")
            .street("test")
            .detailStreet("test")
            .build()));

        //when
        RequestBuilder requestBuilder = get("/posts/random/1")
            .contentType(APPLICATION_JSON);
        //then
        mockMvc.perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andDo(print());
    }
}