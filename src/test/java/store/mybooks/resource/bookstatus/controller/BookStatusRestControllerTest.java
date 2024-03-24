package store.mybooks.resource.bookstatus.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.bookstatus.dto.response.BookStatusGetResponse;
import store.mybooks.resource.bookstatus.service.BookStatusService;

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
@WebMvcTest(BookStatusRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class BookStatusRestControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private BookStatusService bookStatusService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

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
                }, new BookStatusGetResponse() {
                    @Override
                    public String getId() {
                        return "재고없음";
                    }
                }, new BookStatusGetResponse() {
                    @Override
                    public String getId() {
                        return "삭제도서";
                    }
                }
        );

        when(bookStatusService.getAllBookStatus()).thenReturn(bookStatusGetResponseList);
        mockMvc.perform(get("/api/books-statuses")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value("판매중"))
                .andExpect(jsonPath("$[1].id").value("판매종료"))
                .andExpect(jsonPath("$[2].id").value("재고없음"))
                .andExpect(jsonPath("$[3].id").value("삭제도서"))
                .andDo(document("bookStatus",
                        responseFields(
                                fieldWithPath("[]").description("도서 상태 목록"),
                                fieldWithPath("[].id").description("도서 상태 ID")
                        )
                ));
        verify(bookStatusService, times(1)).getAllBookStatus();
    }
}