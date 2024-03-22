package store.mybooks.resource.bookcategory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.bookcategory.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.bookcategory.service.BookCategoryService;
import store.mybooks.resource.category.exception.CategoryNotExistsException;

/**
 * packageName    : store.mybooks.resource.book_category.controller
 * fileName       : BookCategoryRestControllerTest
 * author         : damho-lee
 * date           : 2/29/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/29/24          damho-lee          최초 생성
 */
@WebMvcTest(value = BookCategoryRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class BookCategoryRestControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookCategoryService bookCategoryService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("Book-Category 등록 테스트")
    void givenBookCategoryCreateRequest_whenCreateBookCategory_thenReturnCreatedStatusCode() throws Exception {
        List<Integer> categoryIdList = new ArrayList<>();
        categoryIdList.add(1);
        categoryIdList.add(2);
        categoryIdList.add(3);
        BookCategoryCreateRequest bookCategoryCreateRequest = new BookCategoryCreateRequest(1L, categoryIdList);
        String content = objectMapper.writeValueAsString(bookCategoryCreateRequest);
        doNothing().when(bookCategoryService).createBookCategory(any());
        mockMvc.perform(post("/api/book-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andDo(document("book_category-create",
                        requestFields(
                                fieldWithPath("bookId").description("도서 ID"),
                                fieldWithPath("categoryIdList").description("카테고리 ID 리스트")
                        )));
        verify(bookCategoryService, times(1)).createBookCategory(any());
    }

    @Test
    @DisplayName("Book-Category 등록 테스트 실패 - Validation")
    void givenInvalidBookCategoryCreateRequest_whenCreateBookCategory_thenThrowRequestValidationFailedException()
            throws Exception {
        List<Integer> categoryIdList = new ArrayList<>();
        categoryIdList.add(1);
        BookCategoryCreateRequest bookCategoryCreateRequestCategoryListIsNull =
                new BookCategoryCreateRequest(1L, null);
        BookCategoryCreateRequest bookCategoryCreateRequestCategoryListIsEmpty =
                new BookCategoryCreateRequest(2L, new ArrayList<>());
        BookCategoryCreateRequest bookCategoryCreateRequestBookIdIsNull =
                new BookCategoryCreateRequest(null, categoryIdList);
        BookCategoryCreateRequest bookCategoryCreateRequestBookIdIsZero =
                new BookCategoryCreateRequest(0L, categoryIdList);
        BookCategoryCreateRequest bookCategoryCreateRequestBookIdIsNegative =
                new BookCategoryCreateRequest(-1L, categoryIdList);
        String contentCategoryListIsNull = objectMapper.writeValueAsString(bookCategoryCreateRequestCategoryListIsNull);
        String contentCategoryListIsEmpty =
                objectMapper.writeValueAsString(bookCategoryCreateRequestCategoryListIsEmpty);
        String contentBookIdIsNull = objectMapper.writeValueAsString(bookCategoryCreateRequestBookIdIsNull);
        String contentBookIdIsZero = objectMapper.writeValueAsString(bookCategoryCreateRequestBookIdIsZero);
        String contentBookIdIsNegative = objectMapper.writeValueAsString(bookCategoryCreateRequestBookIdIsNegative);

        mockMvc.perform(post("/api/book-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentCategoryListIsNull))
                .andExpect(status().isBadRequest())
                .andDo(document("book_category-create-fail-null-list"));

        mockMvc.perform(post("/api/book-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentCategoryListIsEmpty))
                .andExpect(status().isBadRequest())
                .andDo(document("book_category-create-fail-empty-list"));

        mockMvc.perform(post("/api/book-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentBookIdIsNull))
                .andExpect(status().isBadRequest())
                .andDo(document("book_category-create-fail-bookId-null"));

        mockMvc.perform(post("/api/book-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentBookIdIsZero))
                .andExpect(status().isBadRequest())
                .andDo(document("book_category-create-fail-bookId-zero"));

        mockMvc.perform(post("/api/book-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentBookIdIsNegative))
                .andExpect(status().isBadRequest())
                .andDo(document("book_category-create-fail-bookId-negative"));
    }

    @Test
    @DisplayName("Book-Category 등록 테스트 실패 - Book 이 존재하지 않는 경우")
    void givenNotExistsBookId_whenCreateBookCategory_thenThrowBookNotExistsException() throws Exception {
        doThrow(new BookNotExistException(1L)).when(bookCategoryService).createBookCategory(any());
        BookCategoryCreateRequest bookCategoryCreateRequest =
                new BookCategoryCreateRequest(1L, List.of(1, 2, 3));
        String content = objectMapper.writeValueAsString(bookCategoryCreateRequest);

        mockMvc.perform(post("/api/book-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(document("book_category-create-fail-bookNotExists"));
    }

    @Test
    @DisplayName("Book-Category 등록 테스트 실패 - Category 가 존재하지 않는 경우")
    void givenNotExistsCategoryId_whenCreateBookCategory_thenThrowCategoryNotExistsException() throws Exception {
        doThrow(new CategoryNotExistsException(1)).when(bookCategoryService).createBookCategory(any());
        BookCategoryCreateRequest bookCategoryCreateRequest =
                new BookCategoryCreateRequest(1L, List.of(1, 2, 3));
        String content = objectMapper.writeValueAsString(bookCategoryCreateRequest);

        mockMvc.perform(post("/api/book-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(document("book_category-create-fail-categoryNotExists"));
    }

    @Test
    @DisplayName("deleteBookCategory 테스트")
    void givenBookId_whenDeleteBookCategory_thenReturnStatusCodeOk() throws Exception {
        doNothing().when(bookCategoryService).deleteBookCategory(anyLong());
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/book-category/{bookId}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("book_category-delete",
                        pathParameters(
                                parameterWithName("bookId").description("도서 ID")
                        )));

        verify(bookCategoryService, times(1)).deleteBookCategory(1L);
    }
}