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
        files.add("city", "??????");
        files.add("scale", "84");
        files.add("sido", "?????????");
        files.add("scaleType", "BIG");
        files.add("street", "?????? ??????");
        files.add("detailStreet", "????????????");
        files.add("zipCode", "15125");
        files.add("feature", "feature");
    }

    @Test
    @DisplayName("????????? ????????? ????????????.")
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
            .city("??????")
            .sido("?????????")
            .zipCode("15125")
            .street("?????? ??????")
            .detailStreet("????????????")
            .scale(84)
            .scaleType(type)
            .feature("?????? ??????")
            .thumbNailUrl("http://thumbNail.jpg")
            .advantages(Lists.newArrayList("advance1", "advance2"))
            .disAdvantages(Lists.newArrayList("disAdvance1", "disAdvance2"))
            .images(Lists.newArrayList("http://image1.jpg", "http://image2.jpg"))
            .build();
    }

    @Test
    @DisplayName("???????????? - 4?????? ???????????? ???????????? ????????????.")
    public void readRandomPostLimitTest() throws Exception {
        //given
        given(postService.readRandomPost(anyInt()))
            .willReturn(Lists.newArrayList(
                createDummy(1L, "?????????1", ScaleType.BIG),
                createDummy(2L, "?????????2", ScaleType.MIDDLE)));

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
                        parameterWithName("limit").description("????????? ????????? ??????")
                    ),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].thumbNailUrl").type(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("[].city").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("[].zipCode").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("[].street").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("[].sido").type(JsonFieldType.STRING).description("???/???/???"),
                        fieldWithPath("[].level").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("[].detailStreet").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("[].scale").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("[].scaleType").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].feature").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].advantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("[].disAdvantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("[].images").type(JsonFieldType.ARRAY).description("??????????????? ?????????")
                    )
                )
            );
    }

    @Test
    @DisplayName("?????? ????????? - BIG, MIDDLE ????????? limit?????? ??????????????? ????????????.")
    public void readRandomPostScaleTypeLimitTest() throws Exception {
        //given
        PostScaleTypeResponse scaleTypeResponse = PostScaleTypeResponse.builder()
            .bigs(Lists.newArrayList(
                createDummy(1L, "?????????1", ScaleType.BIG),
                createDummy(2L, "?????????2", ScaleType.BIG)))
            .middles(Lists.newArrayList(
                createDummy(1L, "?????????1", ScaleType.MIDDLE),
                createDummy(2L, "?????????2", ScaleType.MIDDLE)))
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
                        parameterWithName("limit").description("????????? ????????? ????????? ??????"),
                        parameterWithName("scaleTypes").description("??????????????? ??????(???????????? ??????) `BIG`, `MIDDLE`, `SMALL`")
                            .optional()
                    ),
                    responseFields(
                        fieldWithPath("bigs.[].id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("bigs.[].title").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("bigs.[].thumbNailUrl").type(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("bigs.[].city").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("bigs.[].zipCode").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("bigs.[].street").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("bigs.[].sido").type(JsonFieldType.STRING).description("???/???/???"),
                        fieldWithPath("bigs.[].level").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("bigs.[].detailStreet").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("bigs.[].scale").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("bigs.[].scaleType").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("bigs.[].feature").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("bigs.[].advantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("bigs.[].disAdvantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("bigs.[].images").type(JsonFieldType.ARRAY).description("??????????????? ?????????"),

                        fieldWithPath("middles.[].id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("middles.[].title").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("middles.[].thumbNailUrl").type(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("middles.[].city").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("middles.[].zipCode").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("middles.[].street").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("middles.[].sido").type(JsonFieldType.STRING).description("???/???/???"),
                        fieldWithPath("middles.[].level").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("middles.[].detailStreet").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("middles.[].scale").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("middles.[].scaleType").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("middles.[].feature").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("middles.[].advantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("middles.[].disAdvantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("middles.[].images").type(JsonFieldType.ARRAY).description("??????????????? ?????????"),

                        fieldWithPath("smalls").type(JsonFieldType.OBJECT).description("????????? ?????? ??? ?????????").optional()
                    )
                )
            );
    }

    @Test
    @DisplayName("??????????????? ????????? ????????????.")
    public void searchTest() throws Exception {
        //given
        given(postService.searchTitle(anyString(), any()))
            .willReturn(Lists.newArrayList(
                createDummy(1L, "?????? ???????????????", ScaleType.BIG),
                createDummy(2L, "?????? ???????????????(?????????)", ScaleType.MIDDLE)
            ));

        //when
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("search", "??????");
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
                        parameterWithName("search").description("?????? ?????????"),
                        parameterWithName("page").description("????????? ?????? `1?????? ??????`").optional(),
                        parameterWithName("size").description("?????? ??????").optional()
                    ),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].thumbNailUrl").type(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("[].city").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("[].zipCode").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("[].street").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("[].sido").type(JsonFieldType.STRING).description("???/???/???"),
                        fieldWithPath("[].level").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("[].detailStreet").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("[].scale").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("[].scaleType").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].feature").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].advantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("[].disAdvantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("[].images").type(JsonFieldType.ARRAY).description("??????????????? ?????????")
                    )
                )
            );
    }

    @Test
    @DisplayName("??????????????? ??????????????? ????????????.")
    public void readFilterPostTest() throws Exception {
        //given
        given(postService.searchFilter(anyString(), any(), any()))
            .willReturn(Lists.newArrayList(
                createDummy(1L, "?????????1", ScaleType.BIG),
                createDummy(2L, "?????????2", ScaleType.BIG)));

        //when
        MultiValueMap<String, String> querParams = new LinkedMultiValueMap<>();
        querParams.add("sidos", "?????????");
        querParams.add("scaleTypes", "BIG");

        RequestBuilder requestBuilder = RestDocumentationRequestBuilders.get("/search/{city}/posts", "??????")
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
                        parameterWithName("city").description("???/???")
                    ),
                    requestParameters(
                        parameterWithName("sidos").description("???/???/???").optional(),
                        parameterWithName("scaleTypes").description("??????????????? ?????? `ALL`, `BIG`, `MIDDLE`, `SMALL`"),
                        parameterWithName("page").description("????????? ?????? `1?????? ??????`").optional(),
                        parameterWithName("size").description("?????? ??????").optional()
                    ),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].thumbNailUrl").type(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("[].city").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("[].zipCode").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("[].street").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("[].sido").type(JsonFieldType.STRING).description("???/???/???"),
                        fieldWithPath("[].level").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("[].detailStreet").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("[].scale").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("[].scaleType").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].feature").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("[].advantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("[].disAdvantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("[].images").type(JsonFieldType.ARRAY).description("??????????????? ?????????")
                    )
                )
            );
    }

    @Test
    @DisplayName("????????? ????????? ????????????.")
    void createPostTest() throws Exception{
        //given
        given(postService.createNewPost(any(PostRequest.class))).willReturn(1L);

        //when
        RequestBuilder requestBuilder = multipart("/posts")
            .file("images", "image1".getBytes())
            .file("images", "image2".getBytes())
            .file("thumbNailImage", "thumbNailImage".getBytes())
            .contentType(APPLICATION_JSON)
            .param("advantages", new String[]{"??????1", "??????2"})
            .param("disAdvantages", new String[]{"??????1", "??????2"})
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
                        partWithName("images").description("?????????1"),
                        partWithName("thumbNailImage").description("????????? ?????????")
                    ),
                    requestParameters(
                        parameterWithName("title").description("??????????????? ??????"),
                        parameterWithName("city").description("??????"),
                        parameterWithName("zipCode").description("????????????"),
                        parameterWithName("street").description("??????"),
                        parameterWithName("sido").description("???/???/???"),
                        parameterWithName("level").description("?????????"),
                        parameterWithName("detailStreet").description("?????? ??????"),
                        parameterWithName("scale").description("??????"),
                        parameterWithName("scaleType").description("??????????????? ?????? `ALL`, `BIG`, `MIDDLE`, `SMALL`"),
                        parameterWithName("feature").description("??????????????? ??????"),
                        parameterWithName("advantages").description("??????????????? ??????"),
                        parameterWithName("disAdvantages").description("??????????????? ??????")
                    ),
                    responseFields(
                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("????????? ????????? ?????????")
                    )
                )
            );
    }


    @Test
    @DisplayName("????????? ????????? ????????????.")
    public void updatePostTest() throws Exception {
        //given
        given(postService.updatePost(anyLong(), any()))
            .willReturn(createDummy(1L, "???????????? ?????????", ScaleType.BIG));

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
                        parameterWithName("postId").description("????????? ????????? ?????????")
                    ),
                    requestParts(
                        partWithName("images").description("?????????1"),
                        partWithName("thumbNailImage").description("????????? ?????????")
                    ),
                    requestParameters(
                        parameterWithName("title").description("??????????????? ??????"),
                        parameterWithName("city").description("??????"),
                        parameterWithName("zipCode").description("????????????"),
                        parameterWithName("street").description("??????"),
                        parameterWithName("sido").description("???/???/???"),
                        parameterWithName("level").description("?????????"),
                        parameterWithName("detailStreet").description("?????? ??????"),
                        parameterWithName("scale").description("??????"),
                        parameterWithName("scaleType").description("??????????????? ?????? `ALL`, `BIG`, `MIDDLE`, `SMALL`"),
                        parameterWithName("feature").description("??????????????? ??????"),
                        parameterWithName("advantages").description("??????????????? ??????"),
                        parameterWithName("disAdvantages").description("??????????????? ??????")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("thumbNailUrl").type(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("city").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("zipCode").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("street").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("sido").type(JsonFieldType.STRING).description("???/???/???"),
                        fieldWithPath("level").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("detailStreet").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("scale").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("scaleType").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("feature").type(JsonFieldType.STRING).description("??????????????? ??????"),
                        fieldWithPath("advantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("disAdvantages").type(JsonFieldType.ARRAY).description("??????????????? ??????"),
                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("??????????????? ?????????")
                    )
                )
            );
    }

    @Test
    @DisplayName("????????? ????????? ????????????.")
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
                        parameterWithName("postId").description("????????? ????????? ?????????")
                    )
                )
            );

    }
}