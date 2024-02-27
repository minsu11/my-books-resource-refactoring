package store.mybooks.resource.author.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.author.dto.request.AuthorCreateRequest;
import store.mybooks.resource.author.dto.request.AuthorModifyRequest;
import store.mybooks.resource.author.dto.response.AuthorCreateResponse;
import store.mybooks.resource.author.dto.response.AuthorDeleteResponse;
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
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthorRestControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthorMapper authorMapper;

    @MockBean
    private AuthorService authorService;

    private final String url = "/api/authors";
    private Author author;
    private final Integer authorId = 1;
    private final Integer authorId2 = 2;
    private final String authorName = "authorName";
    private final String authorname2 = "authorName2";
    private final String authorContent = "authorContent";
    private final String authorContent2 = "authorContent2";

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
        author = new Author(authorName, authorContent);
    }

//    @Test
//    @DisplayName("전체 저자 조회")
//    void givenAuthorList_whenFindAllAuthors_thenReturnAllAuthorsGetResponse() throws Exception {
//        Integer page = 0;
//        Integer size = 2;
//        Pageable pageable = PageRequest.of(page, size);
//        List<AuthorGetResponse> authorGetResponseList =
//                Arrays.asList(new AuthorGetResponse(bookId, bookName, bookContent), new AuthorGetResponse(bookId2,
//                        bookName2, bookContent2));
//
//        Page<AuthorGetResponse> authorGetResponsePage = new PageImpl<>(authorGetResponseList, pageable,
//                authorGetResponseList.size());
//        when(authorService.getAllAuthors(pageable)).thenReturn(authorGetResponsePage);
//        mockMvc.perform(get(url + "?page=" + page + "&size=" + size)
//                        .accept("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].id").value(bookId))
//                .andExpect(jsonPath("$.content[0].name").value(bookName))
//                .andExpect(jsonPath("$.content[0].content").value(bookContent))
//                .andExpect(jsonPath("$.content[1].id").value(bookId2))
//                .andExpect(jsonPath("$.content[1].name").value(bookName2))
//                .andExpect(jsonPath("$.content[1].content").value(bookContent2));
//
//        verify(authorService, times(1)).getAllAuthors(pageable);
//    }

//    @Test
//    @DisplayName("저자 조회")
//    void givenAuthorId_whenFindAuthor_thenReturnAuthorGetResponse() throws Exception {
//        AuthorGetResponse authorGetResponse =
//                new AuthorGetResponse(author.getId(), author.getName(), author.getContent());
//
//        when(authorService.getAuthor(bookId)).thenReturn(authorGetResponse);
//
//        mockMvc.perform(get(url + "/{id}", bookId)
//                        .accept("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(authorGetResponse.getId()))
//                .andExpect(jsonPath("$.name").value(authorGetResponse.getName()))
//                .andExpect(jsonPath("$.content").value(authorGetResponse.getContent()))
//                .andDo(document("author-get",
//                        pathParameters(
//                                parameterWithName("id").description("저자 ID")
//                        ),
//                        responseFields(
//                                fieldWithPath("id").description("도서 ID"),
//                                fieldWithPath("name").description("도서명"),
//                                fieldWithPath("content").description("도서 설명")
//                        )));
//
//        verify(authorService, times(1)).getAuthor(bookId);
//    }

    @Test
    @DisplayName("저자 등록(검증 통과)")
    void givenValidAuthorCreateRequest_whenCreateAuthor_thenSaveAuthorAndReturnAuthorCreateResponse()
            throws Exception {
        AuthorCreateRequest createRequest = new AuthorCreateRequest(authorName, authorContent);

        AuthorCreateResponse createResponse = new AuthorCreateResponse();
        createResponse.setName(createRequest.getName());
        createResponse.setContent(createRequest.getContent());

        given(authorMapper.createResponse(author)).willReturn(createResponse);

        when(authorService.createAuthor(any(AuthorCreateRequest.class))).thenReturn(createResponse);

        mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createResponse.getName()))
                .andExpect(jsonPath("$.content").value(createResponse.getContent()))
                .andDo(document("author-create",
                        requestParameters(
                                parameterWithName("name").description("등록할 저자명"),
                                parameterWithName("content").description("저자 소개글")
                        ),
                        responseFields(
                                fieldWithPath("name").description("저자명"),
                                fieldWithPath("content").description("저자 소개글")
                        )));
        verify(authorService, times(1)).createAuthor(any(AuthorCreateRequest.class));
    }

    @Test
    @DisplayName("저자 등록(검증 실패)")
    void givenInvalidAuthorCreateRequest_whenCreateAuthor_thenThrowBindException()
            throws Exception {
        AuthorCreateRequest createRequest = new AuthorCreateRequest("", authorContent);
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
        AuthorModifyRequest modifyRequest = new AuthorModifyRequest(authorname2, authorContent2);
        Author resultAuthor = new Author(modifyRequest.getChangeName(), modifyRequest.getChangeContent());

        AuthorModifyResponse modifyResponse = new AuthorModifyResponse();
        modifyResponse.setChangedName(modifyRequest.getChangeName());
        modifyResponse.setChangedContent(modifyRequest.getChangeContent());

        given(authorMapper.modifyResponse(resultAuthor)).willReturn(modifyResponse);

        when(authorService.modifyAuthor(eq(authorId), any(AuthorModifyRequest.class))).thenReturn(modifyResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.put(url + "/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.changedName").value(modifyResponse.getChangedName()))
                .andExpect(jsonPath("$.changedContent").value(modifyResponse.getChangedContent()))
                .andDo(document("author-modify",
                        pathParameters(
                                parameterWithName("id").description("저자 ID")),
                        responseFields(
                                fieldWithPath("changedName").description("수정한 저자명"),
                                fieldWithPath("changedContent").description("저자 소개글")
                        )
                ));

        verify(authorService, times(1)).modifyAuthor(eq(authorId), any(AuthorModifyRequest.class));
    }

    @Test
    @DisplayName("저자 수정(검증 실패)")
    void givenAuthorIdAndInvalidAuthorModifyRequest_whenModifyAuthor_thenThrowBindException() throws Exception {
        AuthorModifyRequest modifyRequest = new AuthorModifyRequest("", authorContent2);
        AuthorModifyResponse modifyResponse = new AuthorModifyResponse();

        when(authorService.modifyAuthor(eq(authorId), any(AuthorModifyRequest.class))).thenReturn(modifyResponse);

        mockMvc.perform(put(url + "/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequest)))
                .andExpect(status().isBadRequest());

        verify(authorService, times(0)).modifyAuthor(eq(authorId), any(AuthorModifyRequest.class));
    }

    @Test
    @DisplayName("저자 삭제")
    void givenAuthorId_whenDeleteAuthor_thenDeleteAuthorAndReturnAuthorDeleteResponse() throws Exception {
        AuthorDeleteResponse deleteResponse = new AuthorDeleteResponse();
        deleteResponse.setName(authorName);
        given(authorMapper.deleteResponse(author)).willReturn(deleteResponse);

        when(authorService.deleteAuthor(authorId)).thenReturn(deleteResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.delete(url + "/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(deleteResponse.getName()))
                .andDo((document("author-delete",
                        pathParameters(
                                parameterWithName("id").description("저자 ID")),
                        responseFields(
                                fieldWithPath("name").description("삭제된 도서명")
                        ))));

        verify(authorService, times(1)).deleteAuthor(authorId);
    }
}