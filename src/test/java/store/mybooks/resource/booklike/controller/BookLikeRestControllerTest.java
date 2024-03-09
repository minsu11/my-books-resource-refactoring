package store.mybooks.resource.booklike.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.booklike.service.BookLikeService;

/**
 * packageName    : store.mybooks.resource.book_like.controller <br/>
 * fileName       : BookLikeRestControllerTest<br/>
 * author         : newjaehun <br/>
 * date           : 3/7/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/7/24        newjaehun       최초 생성<br/>
 */
@WebMvcTest(BookLikeRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class BookLikeRestControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private BookLikeService bookLikeService;

    private final String url = "/api/book-likes";

    private final Long userId = 1L;
    private final Long bookId = 2L;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("사용자가 좋아요한 페이징된 도서 목록")
    void givenUserId_whenGetUserBookLike_thenReturnPagedBookBriefResponse() throws Exception {
        int page = 0;
        int size = 2;
        Long bookId2 = 4L;
        String name = "bookName";
        String name2 = "bookName2";
        Integer cost = 10000;
        Integer cost2 = 20000;
        Pageable pageable = PageRequest.of(page, size);
        List<BookBriefResponse> lists =
                Arrays.asList(new BookBriefResponse(bookId, name, cost), new BookBriefResponse(bookId2, name2, cost2));

        Page<BookBriefResponse> response = new PageImpl<>(lists, pageable, size);

        when(bookLikeService.getUserBookLike(eq(userId), any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get(url + "?page=" + page + "&size=" + size)
                        .header("X-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(size))
                .andExpect(jsonPath("$.content[0].id").value(bookId))
                .andExpect(jsonPath("$.content[0].name").value(name))
                .andExpect(jsonPath("$.content[0].saleCost").value(cost))
                .andExpect(jsonPath("$.content[1].id").value(bookId2))
                .andExpect(jsonPath("$.content[1].name").value(name2))
                .andExpect(jsonPath("$.content[1].saleCost").value(cost2))
                .andDo(document("bookLike-getBookListByUserId",
                        requestHeaders(
                                headerWithName("X-User-Id").description("User ID")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].id").description("도서 ID"),
                                fieldWithPath("content[].name").description("도서명"),
                                fieldWithPath("content[].saleCost").description("도서 판매가"),
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
        verify(bookLikeService, times(1)).getUserBookLike(eq(userId), any(Pageable.class));
    }

    @Test
    @DisplayName("사용자가 도서 좋아요 요청")
    void givenUserIdAndBookId_whenUpdateUserBookLike_thenReturnTrue() throws Exception {

        when(bookLikeService.updateUserBookLike(userId, bookId)).thenReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders.post(url + "/{bookId}", bookId)
                        .header("X-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(document("bookLike-update",
                        requestHeaders(
                                headerWithName("X-User-Id").description("User ID")
                        ),
                        pathParameters(
                                parameterWithName("bookId").description("도서 ID")
                        )
                ));

        verify(bookLikeService, times(1)).updateUserBookLike(userId, bookId);
    }

}