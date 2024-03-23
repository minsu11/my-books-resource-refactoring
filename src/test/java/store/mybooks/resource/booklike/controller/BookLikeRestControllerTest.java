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
    private final Long bookId = 5L;

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
        int page = 0, size = 2;

        BookBriefResponse response1 = new BookBriefResponse();
        response1.setId(bookId);
        response1.setImage("imageUrl1");
        response1.setName("name1");
        response1.setRate(1.0);
        response1.setReviewCount(1L);
        response1.setCost(10000);
        response1.setSaleCost(9000);

        BookBriefResponse response2 = new BookBriefResponse();
        response2.setId(2L);
        response2.setImage("imageUrl2");
        response2.setName("name2");
        response2.setRate(2.0);
        response2.setReviewCount(2L);
        response2.setCost(20000);
        response2.setSaleCost(19000);

        Page<BookBriefResponse> response =
                new PageImpl<>(Arrays.asList(response1, response2), PageRequest.of(page, size), size);

        when(bookLikeService.getUserBookLike(eq(userId), any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get(url + "?page=" + page + "&size=" + size)
                        .header("X-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(size))
                .andExpect(jsonPath("$.content[0].id").value(response1.getId()))
                .andExpect(jsonPath("$.content[0].image").value(response1.getImage()))
                .andExpect(jsonPath("$.content[0].name").value(response1.getName()))
                .andExpect(jsonPath("$.content[0].rate").value(response1.getRate()))
                .andExpect(jsonPath("$.content[0].reviewCount").value(response1.getReviewCount()))
                .andExpect(jsonPath("$.content[0].cost").value(response1.getCost()))
                .andExpect(jsonPath("$.content[0].saleCost").value(response1.getSaleCost()))
                .andExpect(jsonPath("$.content[1].id").value(response2.getId()))
                .andExpect(jsonPath("$.content[1].image").value(response2.getImage()))
                .andExpect(jsonPath("$.content[1].name").value(response2.getName()))
                .andExpect(jsonPath("$.content[1].rate").value(response2.getRate()))
                .andExpect(jsonPath("$.content[1].reviewCount").value(response2.getReviewCount()))
                .andExpect(jsonPath("$.content[1].cost").value(response2.getCost()))
                .andExpect(jsonPath("$.content[1].saleCost").value(response2.getSaleCost()))
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
                                fieldWithPath("content[].image").description("도서 썸네일 이미지 URL"),
                                fieldWithPath("content[].name").description("도서명"),
                                fieldWithPath("content[].rate").description("도서 총평점"),
                                fieldWithPath("content[].reviewCount").description("도서 리뷰 갯수"),
                                fieldWithPath("content[].cost").description("도서 정가"),
                                fieldWithPath("content[].saleCost").description("도서 판매가"),
                                fieldWithPath("pageable").description("페이지정보"),
                                fieldWithPath("pageable.sort").description("페이지 정렬 정보"),
                                fieldWithPath("pageable.sort.sorted").description("페이지 정렬되었는지 여부(true: 정렬 됨)"),
                                fieldWithPath("pageable.sort.unsorted").description("페이지 정렬되지 않았는지 여부(true: 정렬 안 됨)"),
                                fieldWithPath("pageable.sort.empty").description("페이지 정렬 정보가 비어 있는지 여부(true: 비어있음)"),
                                fieldWithPath("pageable.pageSize").description("전체 페이지 수"),
                                fieldWithPath("pageable.pageNumber").description("현재 페이지 번호(0부터 시작)"),
                                fieldWithPath("pageable.offset").description("현재 페이지의 시작 오프셋(0부터 시작)"),
                                fieldWithPath("pageable.paged").description("페이지네이션을 사용하는지 여부(true: 사용함)"),
                                fieldWithPath("pageable.unpaged").description("페이지네이션을 사용하는지 여부(true: 사용 안 함)"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 요소(항목) 수"),
                                fieldWithPath("last").description("마지막 페이지 여부(true: 마지막 페이지)"),
                                fieldWithPath("numberOfElements").description("혀재 페이지의 요소(항목) 수"),
                                fieldWithPath("size").description("페이지 당 요소(항목) 수"),
                                fieldWithPath("sort").description("결과 정렬 정보를 담은 객체"),
                                fieldWithPath("sort.sorted").description("결과가 정렬되었는지 여부(true: 정렬 됨)"),
                                fieldWithPath("sort.unsorted").description("결과가 정렬되지 않았는지 여부(true: 정렬 안 됨)"),
                                fieldWithPath("sort.empty").description("결과 정렬 정보가 비어 있는지 여부(true: 비어있음)"),
                                fieldWithPath("number").description("현재 페이지 번호(0부터 시작)"),
                                fieldWithPath("first").description("첫 페이지 여부(true: 첫 페이지)"),
                                fieldWithPath("empty").description("결과가 비어 있는지 여부(true: 비어있음)")
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

    @Test
    @DisplayName("사용자가 해당도서 좋아요 유무조회")
    void givenUserIdAndBookId_whenIsUserBookLike_thenReturnTrue() throws Exception {
        when(bookLikeService.isUserBookLike(userId, bookId)).thenReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{bookId}", bookId)
                        .header("X-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(document("bookLike-isLike",
                        requestHeaders(
                                headerWithName("X-User-Id").description("User ID")
                        ),
                        pathParameters(
                                parameterWithName("bookId").description("도서 ID")
                        )
                ));

        verify(bookLikeService, times(1)).isUserBookLike(userId, bookId);
    }

}