package com.example.climblabs.post.web;


import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.PostApiResponse;
import com.example.climblabs.post.web.dto.PostRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest
class PostApiControllerTest {

    private final String APPLICATION_JSON = "application/json;charset=UTF-8";

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    MultiValueMap<String, String> files;

    @MockBean
    private PostService postService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
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

    private PostApiResponse createDummy(long id, String title) {
        return PostApiResponse.builder()
                .id(id)
                .title(title)
                .climbingTitle("클라이밍장")
                .level(3)
                .city("경기")
                .zipCode("15125")
                .street("거리 주소")
                .detailStreet("상세주소")
                .scaleType(ScaleType.BIG)
                .feature("암장 특징")
                .advantages(Lists.newArrayList("advance1", "advance2"))
                .disAdvantages(Lists.newArrayList("disAdvance1", "disAdvance2"))
                .images(Lists.newArrayList("image1", "image2"))
                .build();
    }

    @Test
    @DisplayName("여긴어때 - 4개의 게시물을 무작위로 요청한다.")
    public void readRandomPostLimitTest() throws Exception {
        //given
        given(postService.readRandomPost(anyInt()))
                .willReturn(Lists.newArrayList(createDummy(1L, "게시물1"), createDummy(2L, "게시물2")));

        //when
        RequestBuilder requestBuilder = RestDocumentationRequestBuilders.get("/posts/random/{limit}", 2)
                .contentType(APPLICATION_JSON);
        //then
        ResultActions result = mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("readRandomPostLimit",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("limit").description("수신할 게시물 갯수")
                                ),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                        fieldWithPath("[].climbingTitle").type(JsonFieldType.STRING).description("클라이밍장 이름"),
                                        fieldWithPath("[].city").type(JsonFieldType.STRING).description("지역"),
                                        fieldWithPath("[].zipCode").type(JsonFieldType.STRING).description("우편번호"),
                                        fieldWithPath("[].street").type(JsonFieldType.STRING).description("위치"),
                                        fieldWithPath("[].level").type(JsonFieldType.NUMBER).description("난이도"),
                                        fieldWithPath("[].detailStreet").type(JsonFieldType.STRING).description("상세 위치치"),
                                        fieldWithPath("[].scaleType").type(JsonFieldType.STRING).description("클라이밍장 크기"),
                                        fieldWithPath("[].feature").type(JsonFieldType.STRING).description("클라이밍장 특징"),
                                        fieldWithPath("[].advantages").type(JsonFieldType.ARRAY).description("클라이밍장 장점"),
                                        fieldWithPath("[].disAdvantages").type(JsonFieldType.ARRAY).description("클라이밍장 단점"),
                                        fieldWithPath("[].images").type(JsonFieldType.ARRAY).description("클라이밍장 이미지")
                                )
                        )
                );
    }
}