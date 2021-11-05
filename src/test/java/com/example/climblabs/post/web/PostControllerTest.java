package com.example.climblabs.post.web;


import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.web.dto.PostRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final String APPLICATION_JSON = "application/json;charset=UTF-8";
    
    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    private PostRequest postRequest;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        List<String> advantages = Lists.newArrayList("advantage1","advantage2");
        List<String> disAdvantages = Lists.newArrayList("disAdvantage1","disAdvantage2");
        List<String> images = Lists.newArrayList("url1","url2");

        postRequest = PostRequest.builder()
                .title("test")
                .climbingTitle("test")
                .level(1)
                .location("test")
                .size("test")
                .feature("test")
                .advantages(advantages)
                .disAdvantages(disAdvantages)
                .images(images).build();
    }

    @Test
    @DisplayName("게시물 등록에 성공한다.")
    public void createSuccessPost() throws Exception {
        RequestBuilder requestBuilder = post("/posts")
                .contentType(APPLICATION_JSON)
                .content(toJson(postRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.postId").exists())
                .andDo(print());

    }

    private <T> String toJson(T obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}