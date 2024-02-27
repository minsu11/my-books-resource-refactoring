package store.mybooks.resource.book_status.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.book_status.dto.response.BookStatusGetResponse;
import store.mybooks.resource.book_status.service.BookStatusService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName    : store.mybooks.resource.book_status.controller <br/>
 * fileName       : BookStatusRestControllerTest<br/>
 * author         : newjaehun <br/>
 * date           : 2/23/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/23/24        newjaehun       최초 생성<br/>
 */
@WebMvcTest(value = BookStatusRestController.class,excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class BookStatusRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookStatusService bookStatusService;

    private final String url = "/api/books-statuses";


    @Test
    @DisplayName("전체 도서상태 조회")
    void whenFindAllBookStatuses_thenReturnBookStatusGetResponseList() throws Exception {
        List<BookStatusGetResponse> bookStatusGetResponseList = Arrays.asList(
                new BookStatusGetResponse() {
                    @Override
                    public String getId() {
                        return "판매중";
                    }
                }, new BookStatusGetResponse() {
                    @Override
                    public String getId() {
                        return "판매종료";
                    }
                }
        );

        when(bookStatusService.getAllBookStatus()).thenReturn(bookStatusGetResponseList);
        mockMvc.perform(get(url)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        verify(bookStatusService, times(1)).getAllBookStatus();
    }
}