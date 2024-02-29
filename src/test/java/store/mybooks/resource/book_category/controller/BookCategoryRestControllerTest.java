package store.mybooks.resource.book_category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.book_category.dto.request.BookCategoryCreateRequest;
import store.mybooks.resource.book_category.service.BookCategoryService;

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
class BookCategoryRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookCategoryService bookCategoryService;

    @Test
    @DisplayName("createBookCategory 테스트")
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
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("deleteBookCategory 테스트")
    void givenBookId_whenDeleteBookCategory_thenReturnStatusCodeOk() throws Exception {
        doNothing().when(bookCategoryService).deleteBookCategory(anyLong());
        mockMvc.perform(delete("/api/book-category/{bookId}", 1))
                .andExpect(status().isOk());
    }
}