package store.mybooks.resource.bookauthor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
import store.mybooks.resource.bookauthor.dto.request.BookAuthorCreateRequest;
import store.mybooks.resource.bookauthor.service.BookAuthorService;

/**
 * packageName    : store.mybooks.resource.book_author.controller <br/>
 * fileName       : BookAuthorRestControllerTest<br/>
 * author         : newjaehun <br/>
 * date           : 3/1/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/1/24        newjaehun       최초 생성<br/>
 */
@WebMvcTest(BookAuthorRestController.class)
@ExtendWith(RestDocumentationExtension.class)
class BookAuthorRestControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookAuthorService bookAuthorService;

    private final String url = "/api/book-author";

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("도서저자 추가(검증 성공)")
    void givenBookAuthorCreateRequest_whenCreateBookAuthor_thenReturnStatusCreated() throws Exception {
        List<Integer> authorIdList = new ArrayList<>(List.of(1, 2, 3));

        BookAuthorCreateRequest request = new BookAuthorCreateRequest(1L, authorIdList);


        doNothing().when(bookAuthorService).createBookAuthor(any(BookAuthorCreateRequest.class));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("book_author-create",
                        requestFields(
                                fieldWithPath("bookId").description("도서 ID"),
                                fieldWithPath("authorIdList").description("저자 ID 리스트")
                        )));
        verify(bookAuthorService, times(1)).createBookAuthor(any(BookAuthorCreateRequest.class));

    }

    @Test
    @DisplayName("도서저자 추가(검증 실패)")
    void givenInvalidBookAuthorCreateRequest_whenCreateBookAuthor_thenThrowBindException() throws Exception {
        BookAuthorCreateRequest request = new BookAuthorCreateRequest(1L, null);
        doNothing().when(bookAuthorService).createBookAuthor(any(BookAuthorCreateRequest.class));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(bookAuthorService, times(0)).createBookAuthor(any(BookAuthorCreateRequest.class));
    }

    @Test
    @DisplayName("도서 ID로 전체 도서저자 삭제")
    void givenBookId_whenDeleteBookAuthor_thenReturnStatusOk() throws Exception {
        doNothing().when(bookAuthorService).deleteBookAuthor(anyLong());
        mockMvc.perform(RestDocumentationRequestBuilders.delete(url + "/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(document("book_author-delete",
                        pathParameters(
                                parameterWithName("id").description("도서 ID")
                        )));
        verify(bookAuthorService, times(1)).deleteBookAuthor(anyLong());

    }
}