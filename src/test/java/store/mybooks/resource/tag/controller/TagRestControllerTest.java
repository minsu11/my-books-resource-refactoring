package store.mybooks.resource.tag.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
class TagRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TagService tagService;

    @Test
    @DisplayName("태그 조회")
    void givenGetTags_whenNormalCase_thenReturnIsOk() throws Exception {
        List<TagGetResponse> tagGetResponseList = new ArrayList<>();
        tagGetResponseList.add(new TagGetResponse() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "firstTagName";
            }
        });
        tagGetResponseList.add(new TagGetResponse() {
            @Override
            public Integer getId() {
                return 2;
            }

            @Override
            public String getName() {
                return "secondTagName";
            }
        });
        tagGetResponseList.add(new TagGetResponse() {
            @Override
            public Integer getId() {
                return 3;
            }

            @Override
            public String getName() {
                return "thirdTagName";
            }
        });
        Pageable pageable = PageRequest.of(0, 10);
        when(tagService.getTags(any())).thenReturn(
                new PageImpl<>(tagGetResponseList, pageable, tagGetResponseList.size()));

        mockMvc.perform(get("/api/tags/page?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()").value(tagGetResponseList.size()))
                .andExpect(jsonPath("$.content[0].name").value(tagGetResponseList.get(0).getName()))
                .andExpect(jsonPath("$.content[0].id").value(tagGetResponseList.get(0).getId()))
                .andExpect(jsonPath("$.content[1].name").value(tagGetResponseList.get(1).getName()))
                .andExpect(jsonPath("$.content[1].id").value(tagGetResponseList.get(1).getId()))
                .andExpect(jsonPath("$.content[2].name").value(tagGetResponseList.get(2).getName()))
                .andExpect(jsonPath("$.content[2].id").value(tagGetResponseList.get(2).getId()));
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
                .andExpect(jsonPath("$.name").value(tagCreateRequest.getName()));
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

        mockMvc.perform(put("/api/tags/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tagModifyRequest.getName()));
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

        mockMvc.perform(delete("/api/tags/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tagDeleteResponse.getName()));
    }
}