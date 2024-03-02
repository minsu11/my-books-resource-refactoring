package store.mybooks.resource.tag.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.tag.dto.request.TagCreateRequest;
import store.mybooks.resource.tag.dto.request.TagModifyRequest;
import store.mybooks.resource.tag.dto.response.TagCreateResponse;
import store.mybooks.resource.tag.dto.response.TagDeleteResponse;
import store.mybooks.resource.tag.dto.response.TagGetResponse;
import store.mybooks.resource.tag.dto.response.TagModifyResponse;
import store.mybooks.resource.tag.exception.TagValidationException;
import store.mybooks.resource.tag.service.TagService;

/**
 * packageName    : store.mybooks.resource.tag.controller
 * fileName       : TagRestControllerTest
 * author         : damho-lee
 * date           : 2/21/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/21/24          damho-lee          최초 생성
 */
@WebMvcTest(value = TagRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class TagRestControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TagService tagService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("태그 id로 조회")
    void givenTagId_whenGetTag_thenReturnTagGetResponse() throws Exception {
        TagGetResponse expected = makeTagGetResponse(1, "tagName");
        when(tagService.getTag(1)).thenReturn(expected);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/tags/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andDo(document("tag-get",
                        pathParameters(
                                parameterWithName("id").description("태그 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("태그 ID"),
                                fieldWithPath("name").description("태그 이름")
                        )));
    }

    @Test
    @DisplayName("태그 전부 조회")
    void when_getTags_thenReturnListOfTagGetResponse() throws Exception {
        List<TagGetResponse> expectedList = new ArrayList<>();
        TagGetResponse firstTag = makeTagGetResponse(1, "firstTag");
        TagGetResponse secondTag = makeTagGetResponse(2, "secondTag");
        TagGetResponse thirdTag = makeTagGetResponse(3, "thirdTag");
        expectedList.add(firstTag);
        expectedList.add(secondTag);
        expectedList.add(thirdTag);

        when(tagService.getTags()).thenReturn(expectedList);

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(expectedList.size()))
                .andExpect(jsonPath("$[0].id").value(expectedList.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(expectedList.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(expectedList.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(expectedList.get(1).getName()))
                .andExpect(jsonPath("$[2].id").value(expectedList.get(2).getId()))
                .andExpect(jsonPath("$[2].name").value(expectedList.get(2).getName()))
                .andDo(document("tag-get-list",
                        responseFields(
                                fieldWithPath("[].id").description("태그 ID"),
                                fieldWithPath("[].name").description("태그 이름")
                        )));
    }

    @Test
    @DisplayName("태그 pageable 조회")
    void givenGetTags_whenNormalCase_thenReturnIsOk() throws Exception {
        List<TagGetResponse> tagGetResponseList = new ArrayList<>();
        tagGetResponseList.add(makeTagGetResponse(1, "firstTagName"));
        tagGetResponseList.add(makeTagGetResponse(2, "secondTagName"));
        tagGetResponseList.add(makeTagGetResponse(3, "thirdTagName"));

        Pageable pageable = PageRequest.of(0, 10);
        String content = objectMapper.writeValueAsString(pageable);
        when(tagService.getTags(any())).thenReturn(
                new PageImpl<>(tagGetResponseList, pageable, tagGetResponseList.size()));

        mockMvc.perform(get("/api/tags/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()").value(tagGetResponseList.size()))
                .andExpect(jsonPath("$.content[0].name").value(tagGetResponseList.get(0).getName()))
                .andExpect(jsonPath("$.content[0].id").value(tagGetResponseList.get(0).getId()))
                .andExpect(jsonPath("$.content[1].name").value(tagGetResponseList.get(1).getName()))
                .andExpect(jsonPath("$.content[1].id").value(tagGetResponseList.get(1).getId()))
                .andExpect(jsonPath("$.content[2].name").value(tagGetResponseList.get(2).getName()))
                .andExpect(jsonPath("$.content[2].id").value(tagGetResponseList.get(2).getId()))
                .andDo(document("tag-get-page",
                        requestFields(
                                fieldWithPath("pageNumber").description("페이지"),
                                fieldWithPath("pageSize").description("사이즈"),
                                fieldWithPath("sort.*").ignored(),
                                fieldWithPath("offset").ignored(),
                                fieldWithPath("paged").ignored(),
                                fieldWithPath("unpaged").ignored()
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].id").description("태그 ID"),
                                fieldWithPath("content[].name").description("태그 이름"),
                                fieldWithPath("pageable.sort.*").ignored(),
                                fieldWithPath("pageable.*").ignored(),
                                fieldWithPath("totalElements").ignored(),
                                fieldWithPath("totalPages").ignored(),
                                fieldWithPath("last").ignored(),
                                fieldWithPath("numberOfElements").ignored(),
                                fieldWithPath("size").ignored(),
                                fieldWithPath("number").ignored(),
                                fieldWithPath("first").ignored(),
                                fieldWithPath("sort.*").ignored(),
                                fieldWithPath("empty").ignored()
                        )));
    }

    @Test
    @DisplayName("태그 생성")
    void givenCreateTag_whenNormalCase_thenReturnIsCreated() throws Exception {
        TagCreateRequest tagCreateRequest = new TagCreateRequest("tagName");
        TagCreateResponse tagCreateResponse = new TagCreateResponse();
        tagCreateResponse.setName(tagCreateRequest.getName());
        when(tagService.createTag(any())).thenReturn(tagCreateResponse);

        String content = objectMapper.writeValueAsString(tagCreateRequest);

        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(tagCreateRequest.getName()))
                .andDo(document("tag-create",
                        requestFields(
                                fieldWithPath("name").description("태그 이름")
                        ),
                        responseFields(
                                fieldWithPath("name").description("태그 이름")
                        )));
    }

    @Test
    @DisplayName("태그 생성 - Validation 실패")
    void givenCreateTag_whenValidationFailure_thenReturnBadRequest() throws Exception {
        TagCreateRequest tagCreateRequest = new TagCreateRequest(null);

        String content = objectMapper.writeValueAsString(tagCreateRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(TagValidationException.class);
    }

    @Test
    @DisplayName("태그 수정")
    void givenModifyTag_whenNormalCase_thenReturnIsOk() throws Exception {
        TagModifyRequest tagModifyRequest = new TagModifyRequest("tagName");
        TagModifyResponse tagModifyResponse = new TagModifyResponse();
        tagModifyResponse.setName(tagModifyRequest.getName());
        when(tagService.modifyTag(anyInt(), any())).thenReturn(tagModifyResponse);

        String content = objectMapper.writeValueAsString(tagModifyRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/tags/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tagModifyRequest.getName()))
                .andDo(document("tag-modify",
                        pathParameters(
                                parameterWithName("id").description("태그 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("태그 이름")
                        ),
                        responseFields(
                                fieldWithPath("name").description("태그 이름")
                        )));
    }

    @Test
    @DisplayName("태그 수정 - Validation 실패")
    void givenModifyTag_whenValidationFailure_thenReturnBadRequest() throws Exception {
        TagModifyRequest tagModifyRequest = new TagModifyRequest("   ");

        String content = objectMapper.writeValueAsString(tagModifyRequest);

        MvcResult mvcResult = mockMvc.perform(put("/api/tags/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(TagValidationException.class);
    }

    @Test
    @DisplayName("태그 삭제")
    void givenDeleteTag_whenNormalCase_thenReturnIsOk() throws Exception {
        TagDeleteResponse tagDeleteResponse = new TagDeleteResponse();
        tagDeleteResponse.setName("tagName");
        when(tagService.deleteTag(anyInt())).thenReturn(tagDeleteResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/tags/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tagDeleteResponse.getName()))
                .andDo(document("tag-delete",
                        pathParameters(
                                parameterWithName("id").description("태그 ID")
                        )));
    }

    private TagGetResponse makeTagGetResponse(Integer id, String name) {
        return new TagGetResponse() {
            @Override
            public Integer getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}