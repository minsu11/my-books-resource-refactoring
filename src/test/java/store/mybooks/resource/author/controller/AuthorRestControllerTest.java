package store.mybooks.resource.author.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.author.dto.request.AuthorCreateRequest;
import store.mybooks.resource.author.dto.request.AuthorModifyRequest;
import store.mybooks.resource.author.dto.response.AuthorCreateResponse;
import store.mybooks.resource.author.dto.response.AuthorDeleteResponse;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.dto.response.AuthorModifyResponse;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.author.mapper.AuthorMapper;
import store.mybooks.resource.author.service.AuthorService;

/**
 * packageName    : store.mybooks.resource.author.controller
 * fileName       : AuthorRestControllerTest
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@WebMvcTest(AuthorRestController.class)
@ExtendWith({MockitoExtension.class})
class AuthorRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthorMapper authorMapper;

    @MockBean
    private AuthorService authorService;

    private final String url = "/api/authors";
    private Author author;
    private final Integer id = 1;
    private final Integer id2 = 2;
    private final String name = "authorName";
    private final String name2 = "authorName2";
    private final String content = "authorContent";
    private final String content2 = "authorContent2";

    @BeforeEach
    void setUp() {
        author = new Author(name, content);
    }

    @Test
    @DisplayName("전체 저자 조회")
    void givenAuthorList_whenFindAllAuthors_thenReturnAllAuthorsGetResponse() throws Exception {
        Integer page = 0;
        Integer size = 2;
        Pageable pageable = PageRequest.of(page, size);
        List<AuthorGetResponse> authorGetResponseList =
                Arrays.asList(new AuthorGetResponse(id, name, content), new AuthorGetResponse(id2, name2, content2));

        Page<AuthorGetResponse> authorGetResponsePage = new PageImpl<>(authorGetResponseList, pageable,
                authorGetResponseList.size());
        when(authorService.getAllAuthors(pageable)).thenReturn(authorGetResponsePage);
        mockMvc.perform(get(url + "?page=" + page + "&size=" + size)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].name").value(name))
                .andExpect(jsonPath("$.content[0].content").value(content))
                .andExpect(jsonPath("$.content[1].id").value(id2))
                .andExpect(jsonPath("$.content[1].name").value(name2))
                .andExpect(jsonPath("$.content[1].content").value(content2));

        verify(authorService, times(1)).getAllAuthors(pageable);
    }

    @Test
    @DisplayName("저자 조회")
    void givenAuthorId_whenFindAuthor_thenReturnAuthorGetResponse() throws Exception {
        AuthorGetResponse authorGetResponse =
                new AuthorGetResponse(author.getId(), author.getName(), author.getContent());

        when(authorService.getAuthor(id)).thenReturn(authorGetResponse);

        mockMvc.perform(get(url + "/{id}", id)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorGetResponse.getId()))
                .andExpect(jsonPath("$.name").value(authorGetResponse.getName()))
                .andExpect(jsonPath("$.content").value(authorGetResponse.getContent()));

        verify(authorService, times(1)).getAuthor(id);
    }

    @Test
    @DisplayName("저자 등록(검증 통과)")
    void givenValidAuthorCreateRequest_whenCreateAuthor_thenSaveAuthorAndReturnAuthorCreateResponse()
            throws Exception {
        AuthorCreateRequest createRequest = new AuthorCreateRequest(name, content);

        AuthorCreateResponse createResponse = new AuthorCreateResponse();
        createResponse.setName(createRequest.getName());
        createResponse.setContent(createRequest.getContent());

        when(authorService.createAuthor(any(AuthorCreateRequest.class))).thenReturn(createResponse);
        mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createResponse.getName()))
                .andExpect(jsonPath("$.content").value(createResponse.getContent()));

        verify(authorService, times(1)).createAuthor(any(AuthorCreateRequest.class));

    }

    @Test
    @DisplayName("저자 등록(검증 실패)")
    void givenInvalidAuthorCreateRequest_whenCreateAuthor_thenThrowBindException()
            throws Exception {
        AuthorCreateRequest createRequest = new AuthorCreateRequest("", content);
        AuthorCreateResponse createResponse = new AuthorCreateResponse();

        when(authorService.createAuthor(any(AuthorCreateRequest.class))).thenReturn(createResponse);

        mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(authorService, times(0)).createAuthor(any(AuthorCreateRequest.class));
    }

    @Test
    @DisplayName("저자 수정(검증 성공)")
    void givenAuthorIdAndValidAuthorModifyRequest_whenModifyAuthor_thenModifyAuthorAndReturnAuthorModifyResponse()
            throws Exception {
        AuthorModifyRequest modifyRequest = new AuthorModifyRequest(name2, content2);
        Author resultAuthor = new Author(modifyRequest.getChangeName(), modifyRequest.getChangeContent());

        AuthorModifyResponse modifyResponse = new AuthorModifyResponse();
        modifyResponse.setChangedName(modifyRequest.getChangeName());
        modifyResponse.setChangedContent(modifyRequest.getChangeContent());

        when(authorService.modifyAuthor(eq(id), any(AuthorModifyRequest.class))).thenReturn(modifyResponse);

        mockMvc.perform(put(url + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.changedName").value(modifyResponse.getChangedName()))
                .andExpect(jsonPath("$.changedContent").value(modifyResponse.getChangedContent()));
        verify(authorService, times(1)).modifyAuthor(eq(id), any(AuthorModifyRequest.class));
    }

    @Test
    @DisplayName("저자 수정(검증 실패)")
    void givenAuthorIdAndInvalidAuthorModifyRequest_whenModifyAuthor_thenThrowBindException() throws Exception {
        AuthorModifyRequest modifyRequest = new AuthorModifyRequest("", content2);
        AuthorModifyResponse modifyResponse = new AuthorModifyResponse();

        when(authorService.modifyAuthor(eq(id), any(AuthorModifyRequest.class))).thenReturn(modifyResponse);

        mockMvc.perform(put(url + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequest)))
                .andExpect(status().isBadRequest());

        verify(authorService, times(0)).modifyAuthor(eq(id), any(AuthorModifyRequest.class));
    }

    @Test
    @DisplayName("저자 삭제")
    void givenAuthorId_whenDeleteAuthor_thenDeleteAuthorAndReturnAuthorDeleteResponse() throws Exception {
        AuthorDeleteResponse deleteResponse = new AuthorDeleteResponse();
        deleteResponse.setName(name);

        when(authorService.deleteAuthor(id)).thenReturn(deleteResponse);

        mockMvc.perform(delete(url + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(deleteResponse.getName()));

        verify(authorService, times(1)).deleteAuthor(id);
    }
}