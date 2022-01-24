package com.example.climblabs.post.web;


import com.example.climblabs.post.domain.ScaleType;
import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.request.PostRequest;
import com.example.climblabs.post.web.dto.response.PostResponse;
import com.example.climblabs.post.web.dto.response.PostScaleTypeResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
        files.add("level", "1");
        files.add("title", "climbingTitle");
        files.add("city", "경기");
        files.add("scale", "84");
        files.add("sido", "성남시");
        files.add("scaleType", "BIG");
        files.add("street", "거리 주소");
        files.add("detailStreet", "상세주소");
        files.add("zipCode", "15125");
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

    private PostResponse createDummy(long id, String title, ScaleType type) {
        return PostResponse.builder()
            .id(id)
            .title(title)
            .level(3)
            .city("경기")
            .sido("성남시")
            .zipCode("15125")
            .street("거리 주소")
            .detailStreet("상세주소")
            .scale(84)
            .scaleType(type)
            .feature("암장 특징")
            .thumbNailUrl("http://thumbNail.jpg")
            .advantages(Lists.newArrayList("advance1", "advance2"))
            .disAdvantages(Lists.newArrayList("disAdvance1", "disAdvance2"))
            .images(Lists.newArrayList("http://image1.jpg", "http://image2.jpg"))
            .build();
    }

    @Test
    @DisplayName("여긴어때 - 4개의 게시물을 무작위로 요청한다.")
    public void readRandomPostLimitTest() throws Exception {
        //given
        given(postService.readRandomPost(anyInt()))
            .willReturn(Lists.newArrayList(
                createDummy(1L, "게시물1", ScaleType.BIG),
                createDummy(2L, "게시물2", ScaleType.MIDDLE)));

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
                document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("limit").description("수신할 게시물 갯수")
                    ),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("클라이밍장 이름"),
                        fieldWithPath("[].thumbNailUrl").type(JsonFieldType.STRING).description("썸네일 이미지"),
                        fieldWithPath("[].city").type(JsonFieldType.STRING).description("지역"),
                        fieldWithPath("[].zipCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("[].street").type(JsonFieldType.STRING).description("위치"),
                        fieldWithPath("[].sido").type(JsonFieldType.STRING).description("시/군/구"),
                        fieldWithPath("[].level").type(JsonFieldType.NUMBER).description("난이도"),
                        fieldWithPath("[].detailStreet").type(JsonFieldType.STRING).description("상세 위치치"),
                        fieldWithPath("[].scale").type(JsonFieldType.NUMBER).description("크기"),
                        fieldWithPath("[].scaleType").type(JsonFieldType.STRING).description("클라이밍장 규모"),
                        fieldWithPath("[].feature").type(JsonFieldType.STRING).description("클라이밍장 특징"),
                        fieldWithPath("[].advantages").type(JsonFieldType.ARRAY).description("클라이밍장 장점"),
                        fieldWithPath("[].disAdvantages").type(JsonFieldType.ARRAY).description("클라이밍장 단점"),
                        fieldWithPath("[].images").type(JsonFieldType.ARRAY).description("클라이밍장 이미지")
                    )
                )
            );
    }

    @Test
    @DisplayName("여긴 이만해 - BIG, MIDDLE 타입별 limit만큼 가져오는데 성공한다.")
    public void readRandomPostScaleTypeLimitTest() throws Exception {
        //given
        PostScaleTypeResponse scaleTypeResponse = PostScaleTypeResponse.builder()
            .bigs(Lists.newArrayList(
                createDummy(1L, "게시물1", ScaleType.BIG),
                createDummy(2L, "게시물2", ScaleType.BIG)))
            .middles(Lists.newArrayList(
                createDummy(1L, "게시물1", ScaleType.MIDDLE),
                createDummy(2L, "게시물2", ScaleType.MIDDLE)))
            .build();

        given(postService.readFilterScaleType(anyInt(), any()))
            .willReturn(scaleTypeResponse);

        //when
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("scaleTypes", "BIG");
        queryParams.add("scaleTypes", "MIDDLE");

        RequestBuilder requestBuilder = RestDocumentationRequestBuilders.get("/posts/random")
            .queryParam("limit", "2")
            .queryParams(queryParams)
            .contentType(APPLICATION_JSON);
        //then
        ResultActions result = mockMvc.perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andDo(print());

        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("limit").description("타입별 수신할 게시물 갯수"),
                        parameterWithName("scaleTypes").description("클라이밍장 규모(다중선택 가능) `BIG`, `MIDDLE`, `SMALL`")
                            .optional()
                    ),
                    responseFields(
                        fieldWithPath("bigs.[].id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                        fieldWithPath("bigs.[].title").type(JsonFieldType.STRING).description("클라이밍장 이름"),
                        fieldWithPath("bigs.[].thumbNailUrl").type(JsonFieldType.STRING).description("썸네일 이미지"),
                        fieldWithPath("bigs.[].city").type(JsonFieldType.STRING).description("지역"),
                        fieldWithPath("bigs.[].zipCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("bigs.[].street").type(JsonFieldType.STRING).description("위치"),
                        fieldWithPath("bigs.[].sido").type(JsonFieldType.STRING).description("시/군/구"),
                        fieldWithPath("bigs.[].level").type(JsonFieldType.NUMBER).description("난이도"),
                        fieldWithPath("bigs.[].detailStreet").type(JsonFieldType.STRING).description("상세 위치치"),
                        fieldWithPath("bigs.[].scale").type(JsonFieldType.NUMBER).description("크기"),
                        fieldWithPath("bigs.[].scaleType").type(JsonFieldType.STRING).description("클라이밍장 규모"),
                        fieldWithPath("bigs.[].feature").type(JsonFieldType.STRING).description("클라이밍장 특징"),
                        fieldWithPath("bigs.[].advantages").type(JsonFieldType.ARRAY).description("클라이밍장 장점"),
                        fieldWithPath("bigs.[].disAdvantages").type(JsonFieldType.ARRAY).description("클라이밍장 단점"),
                        fieldWithPath("bigs.[].images").type(JsonFieldType.ARRAY).description("클라이밍장 이미지"),

                        fieldWithPath("middles.[].id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                        fieldWithPath("middles.[].title").type(JsonFieldType.STRING).description("클라이밍장 이름"),
                        fieldWithPath("middles.[].thumbNailUrl").type(JsonFieldType.STRING).description("썸네일 이미지"),
                        fieldWithPath("middles.[].city").type(JsonFieldType.STRING).description("지역"),
                        fieldWithPath("middles.[].zipCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("middles.[].street").type(JsonFieldType.STRING).description("위치"),
                        fieldWithPath("middles.[].sido").type(JsonFieldType.STRING).description("시/군/구"),
                        fieldWithPath("middles.[].level").type(JsonFieldType.NUMBER).description("난이도"),
                        fieldWithPath("middles.[].detailStreet").type(JsonFieldType.STRING).description("상세 위치치"),
                        fieldWithPath("middles.[].scale").type(JsonFieldType.NUMBER).description("크기"),
                        fieldWithPath("middles.[].scaleType").type(JsonFieldType.STRING).description("클라이밍장 규모"),
                        fieldWithPath("middles.[].feature").type(JsonFieldType.STRING).description("클라이밍장 특징"),
                        fieldWithPath("middles.[].advantages").type(JsonFieldType.ARRAY).description("클라이밍장 장점"),
                        fieldWithPath("middles.[].disAdvantages").type(JsonFieldType.ARRAY).description("클라이밍장 단점"),
                        fieldWithPath("middles.[].images").type(JsonFieldType.ARRAY).description("클라이밍장 이미지"),

                        fieldWithPath("smalls").type(JsonFieldType.OBJECT).description("규모가 작은 곳 리스트").optional()
                    )
                )
            );
    }

    @Test
    @DisplayName("클라이밍장 검색에 성공한다.")
    public void searchTest() throws Exception {
        //given
        given(postService.searchTitle(anyString(), any()))
            .willReturn(Lists.newArrayList(
                createDummy(1L, "킹콩 클라이밍장", ScaleType.BIG),
                createDummy(2L, "킹콩 클라이밍장(고암점)", ScaleType.MIDDLE)
            ));

        //when
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("search", "킹콩");
        queryParams.add("page", "1");
        queryParams.add("size", "2");

        RequestBuilder requestBuilder = RestDocumentationRequestBuilders.get("/search/posts")
            .queryParams(queryParams)
            .contentType(APPLICATION_JSON);
        //then
        ResultActions result = mockMvc.perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andDo(print());

        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("search").description("검색 키워드"),
                        parameterWithName("page").description("페이지 번호 `1부터 시작`").optional(),
                        parameterWithName("size").description("표출 갯수").optional()
                    ),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("클라이밍장 이름"),
                        fieldWithPath("[].thumbNailUrl").type(JsonFieldType.STRING).description("썸네일 이미지"),
                        fieldWithPath("[].city").type(JsonFieldType.STRING).description("지역"),
                        fieldWithPath("[].zipCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("[].street").type(JsonFieldType.STRING).description("위치"),
                        fieldWithPath("[].sido").type(JsonFieldType.STRING).description("시/군/구"),
                        fieldWithPath("[].level").type(JsonFieldType.NUMBER).description("난이도"),
                        fieldWithPath("[].detailStreet").type(JsonFieldType.STRING).description("상세 위치치"),
                        fieldWithPath("[].scale").type(JsonFieldType.NUMBER).description("크기"),
                        fieldWithPath("[].scaleType").type(JsonFieldType.STRING).description("클라이밍장 규모"),
                        fieldWithPath("[].feature").type(JsonFieldType.STRING).description("클라이밍장 특징"),
                        fieldWithPath("[].advantages").type(JsonFieldType.ARRAY).description("클라이밍장 장점"),
                        fieldWithPath("[].disAdvantages").type(JsonFieldType.ARRAY).description("클라이밍장 단점"),
                        fieldWithPath("[].images").type(JsonFieldType.ARRAY).description("클라이밍장 이미지")
                    )
                )
            );
    }

    @Test
    @DisplayName("필터검색이 정상적으로 검색된다.")
    public void readFilterPostTest() throws Exception {
        //given
        given(postService.searchFilter(anyString(), any(), any()))
            .willReturn(Lists.newArrayList(
                createDummy(1L, "게시물1", ScaleType.BIG),
                createDummy(2L, "게시물2", ScaleType.BIG)));

        //when
        MultiValueMap<String, String> querParams = new LinkedMultiValueMap<>();
        querParams.add("sidos", "성남시");
        querParams.add("scaleTypes", "BIG");

        RequestBuilder requestBuilder = RestDocumentationRequestBuilders.get("/search/{city}/posts", "경기")
            .queryParams(querParams)
            .contentType(APPLICATION_JSON);
        //then
        ResultActions result = mockMvc.perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andDo(print());

        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("city").description("도/시")
                    ),
                    requestParameters(
                        parameterWithName("sidos").description("시/군/구").optional(),
                        parameterWithName("scaleTypes").description("클라이밍장 규모 `ALL`, `BIG`, `MIDDLE`, `SMALL`"),
                        parameterWithName("page").description("페이지 번호 `1부터 시작`").optional(),
                        parameterWithName("size").description("표출 갯수").optional()
                    ),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("클라이밍장 이름"),
                        fieldWithPath("[].thumbNailUrl").type(JsonFieldType.STRING).description("썸네일 이미지"),
                        fieldWithPath("[].city").type(JsonFieldType.STRING).description("지역"),
                        fieldWithPath("[].zipCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("[].street").type(JsonFieldType.STRING).description("위치"),
                        fieldWithPath("[].sido").type(JsonFieldType.STRING).description("시/군/구"),
                        fieldWithPath("[].level").type(JsonFieldType.NUMBER).description("난이도"),
                        fieldWithPath("[].detailStreet").type(JsonFieldType.STRING).description("상세 위치치"),
                        fieldWithPath("[].scale").type(JsonFieldType.NUMBER).description("크기"),
                        fieldWithPath("[].scaleType").type(JsonFieldType.STRING).description("클라이밍장 규모"),
                        fieldWithPath("[].feature").type(JsonFieldType.STRING).description("클라이밍장 특징"),
                        fieldWithPath("[].advantages").type(JsonFieldType.ARRAY).description("클라이밍장 장점"),
                        fieldWithPath("[].disAdvantages").type(JsonFieldType.ARRAY).description("클라이밍장 단점"),
                        fieldWithPath("[].images").type(JsonFieldType.ARRAY).description("클라이밍장 이미지")
                    )
                )
            );
    }

    @Test
    @DisplayName("게시물 등록에 성공한다.")
    void createPostTest() throws Exception{
        //given
        given(postService.createNewPost(any(PostRequest.class))).willReturn(1L);

        //when
        RequestBuilder requestBuilder = multipart("/posts")
            .file("images", "image1".getBytes())
            .file("images", "image2".getBytes())
            .file("thumbNailImage", "thumbNailImage".getBytes())
            .contentType(APPLICATION_JSON)
            .param("advantages", new String[]{"장점1", "장점2"})
            .param("disAdvantages", new String[]{"단점1", "단점2"})
            .params(files);

        //then
        ResultActions result = mockMvc.perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andDo(print());

        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParts(
                        partWithName("images").description("이미지1"),
                        partWithName("thumbNailImage").description("썸네일 이미지")
                    ),
                    requestParameters(
                        parameterWithName("title").description("클라이밍장 이름"),
                        parameterWithName("city").description("지역"),
                        parameterWithName("zipCode").description("우편번호"),
                        parameterWithName("street").description("위치"),
                        parameterWithName("sido").description("시/군/구"),
                        parameterWithName("level").description("난이도"),
                        parameterWithName("detailStreet").description("상세 위치"),
                        parameterWithName("scale").description("크기"),
                        parameterWithName("scaleType").description("클라이밍장 규모 `ALL`, `BIG`, `MIDDLE`, `SMALL`"),
                        parameterWithName("feature").description("클라이밍장 특징"),
                        parameterWithName("advantages").description("클라이밍장 장점"),
                        parameterWithName("disAdvantages").description("클라이밍장 단점")
                    ),
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("등록된 게시물 아이디")
                    )
                )
            );
    }


    @Test
    @DisplayName("게시물 수정에 성공한다.")
    public void updatePostTest() throws Exception {
        //given
        given(postService.updatePost(anyLong(), any()))
            .willReturn(createDummy(1L, "업데이트 게시물", ScaleType.BIG));

        //when
        MockMultipartHttpServletRequestBuilder fileUpload = (MockMultipartHttpServletRequestBuilder) RestDocumentationRequestBuilders.fileUpload(
            "/posts/{postId}", 1).with(request -> {
            request.setMethod(HttpMethod.PATCH.name());
            return request;
        });

        fileUpload.file("images", "example".getBytes())
            .file("thumbNailImage", "example".getBytes())
            .contentType(APPLICATION_JSON)
            .param("advantages", new String[]{"advantage1", "advantage2"})
            .param("disAdvantages", new String[]{"disAdvantage1", "disAdvantage2"})
            .params(files);

        //then
        ResultActions result = mockMvc.perform(fileUpload)
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andDo(print());

        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("postId").description("수정할 게시물 아이디")
                    ),
                    requestParts(
                        partWithName("images").description("이미지1"),
                        partWithName("thumbNailImage").description("썸네일 이미지")
                    ),
                    requestParameters(
                        parameterWithName("title").description("클라이밍장 이름"),
                        parameterWithName("city").description("지역"),
                        parameterWithName("zipCode").description("우편번호"),
                        parameterWithName("street").description("위치"),
                        parameterWithName("sido").description("시/군/구"),
                        parameterWithName("level").description("난이도"),
                        parameterWithName("detailStreet").description("상세 위치"),
                        parameterWithName("scale").description("크기"),
                        parameterWithName("scaleType").description("클라이밍장 규모 `ALL`, `BIG`, `MIDDLE`, `SMALL`"),
                        parameterWithName("feature").description("클라이밍장 특징"),
                        parameterWithName("advantages").description("클라이밍장 장점"),
                        parameterWithName("disAdvantages").description("클라이밍장 단점")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("클라이밍장 이름"),
                        fieldWithPath("thumbNailUrl").type(JsonFieldType.STRING).description("썸네일 이미지"),
                        fieldWithPath("city").type(JsonFieldType.STRING).description("지역"),
                        fieldWithPath("zipCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("street").type(JsonFieldType.STRING).description("위치"),
                        fieldWithPath("sido").type(JsonFieldType.STRING).description("시/군/구"),
                        fieldWithPath("level").type(JsonFieldType.NUMBER).description("난이도"),
                        fieldWithPath("detailStreet").type(JsonFieldType.STRING).description("상세 위치치"),
                        fieldWithPath("scale").type(JsonFieldType.NUMBER).description("크기"),
                        fieldWithPath("scaleType").type(JsonFieldType.STRING).description("클라이밍장 규모"),
                        fieldWithPath("feature").type(JsonFieldType.STRING).description("클라이밍장 특징"),
                        fieldWithPath("advantages").type(JsonFieldType.ARRAY).description("클라이밍장 장점"),
                        fieldWithPath("disAdvantages").type(JsonFieldType.ARRAY).description("클라이밍장 단점"),
                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("클라이밍장 이미지")
                    )
                )
            );
    }

    @Test
    @DisplayName("게시물 삭제에 성공한다.")
    void deletePostTest() throws Exception{
        //given
        willDoNothing().given(postService).deletePost(anyLong());

        //when
        RequestBuilder requestBuilder = RestDocumentationRequestBuilders.delete("/posts")
            .param("postId", "1");

        ResultActions result = mockMvc.perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andDo(print());

        //then
        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("postId").description("삭제할 게시물 아이디")
                    )
                )
            );

    }
}