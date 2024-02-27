package store.mybooks.resource.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.book.dto.request.BookCreateRequest;
import store.mybooks.resource.book.dto.request.BookModifyRequest;
import store.mybooks.resource.book.dto.response.BookCreateResponse;
import store.mybooks.resource.book.dto.response.BookModifyResponse;
import store.mybooks.resource.book.service.BookService;

/**
 * packageName    : store.mybooks.resource.book.controller <br/>
 * fileName       : BookRestControllerTest<br/>
 * author         : newjaehun <br/>
 * date           : 2/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/25/24        newjaehun       최초 생성<br/>
 */
@WebMvcTest(value = BookRestController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class BookRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private final String url = "/api/books";

    @Test
    @DisplayName("도서 등록(검증 성공)")
    void givenValidBookCreateRequest_whenCreateBook_thenSaveBookAndReturnBookCreateResponse() throws Exception {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true);
        BookCreateResponse response = new BookCreateResponse();
        response.setName(request.getName());

        when(bookService.createBook(any(BookCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.getName()));
        verify(bookService, times(1)).createBook(any(BookCreateRequest.class));
    }

    @Test
    @DisplayName("도서 등록(검증 실패)")
    void givenInValidBookCreateRequest_whenCreateBook_thenThrowBindException() throws Exception {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "123456789876444", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true);
        BookCreateResponse response = new BookCreateResponse();
        response.setName(request.getName());

        when(bookService.createBook(any(BookCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(bookService, times(0)).createBook(any(BookCreateRequest.class));
    }

    @Test
    @DisplayName("도서 수정(검증 성공)")
    void givenBookIdAndValidBookModifyRequest_whenModifyBook_thenModifyBookAndReturnBookModifyResponse()
            throws Exception {
        Long bookId = 3L;
        BookModifyRequest request = new BookModifyRequest("판매종료", 13000, 1, false);
        BookModifyResponse response = new BookModifyResponse();
        response.setName("도서명");

        when(bookService.modifyBook(eq(bookId), any(BookModifyRequest.class))).thenReturn(response);

        mockMvc.perform(put(url + "/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()));

        verify(bookService, times(1)).modifyBook(eq(bookId), any(BookModifyRequest.class));

    }

    @Test
    @DisplayName("도서 수정(검증 실패)")
    void givenBookIdAndInValidBookModifyRequest_whenModifyBook_thenThrowBindException() throws Exception {
        Long bookId = 3L;
        BookModifyRequest request = new BookModifyRequest("판매종료", 12000, -1, false);
        BookModifyResponse response = new BookModifyResponse();
        response.setName("도서명");


        when(bookService.modifyBook(eq(bookId), any(BookModifyRequest.class))).thenReturn(response);

        mockMvc.perform(put(url + "/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(bookService, times(0)).modifyBook(eq(bookId), any(BookModifyRequest.class));
    }
}