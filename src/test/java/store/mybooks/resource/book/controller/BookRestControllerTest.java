package store.mybooks.resource.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
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

@WebMvcTest(BookRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class BookRestControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private final String url = "/api/books";

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("도서 등록(검증 성공)")
    void givenValidBookCreateRequest_whenCreateBook_thenSaveBookAndReturnBookCreateResponse() throws Exception {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));

        BookCreateResponse response = new BookCreateResponse();
        response.setName(request.getName());

        when(bookService.createBook(any(BookCreateRequest.class), any(MultipartFile.class),
                anyList())).thenReturn(response);

        MockMultipartFile requestFile =
                new MockMultipartFile("request", "", "application/json",
                        objectMapper.writeValueAsString(request).getBytes());
        MockMultipartFile thumbnailFile =
                new MockMultipartFile("thumbnail", "thumbnail.png", "image/png", "thumbnail".getBytes());
        MockMultipartFile contentFile =
                new MockMultipartFile("content", "content.png", "image/png", "content".getBytes());
        MockMultipartFile contentFile2 =
                new MockMultipartFile("content", "content2.png", "image/png", "content2".getBytes());

        mockMvc.perform(multipart(url)
                        .file(requestFile)
                        .file(thumbnailFile)
                        .file(contentFile)
                        .file(contentFile2)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andDo(document("book-create",
                        requestParts(
                                partWithName("request").description("도서 생성 요청 본문"),
                                partWithName("thumbnail").description("도서 썸네일 이미지"),
                                partWithName("content").description("도서 내용 파일들")
                        ),
                        requestPartFields("request",
                                fieldWithPath("bookStatusId").description("도서 상태 ID"),
                                fieldWithPath("publisherId").description("출판사 ID"),
                                fieldWithPath("name").description("도서명"),
                                fieldWithPath("isbn").description("ISBN"),
                                fieldWithPath("publishDate").description("출판일"),
                                fieldWithPath("page").description("페이지 수"),
                                fieldWithPath("index").description("목차"),
                                fieldWithPath("explanation").description("도서 설명"),
                                fieldWithPath("originalCost").description("정가"),
                                fieldWithPath("saleCost").description("판매가"),
                                fieldWithPath("stock").description("재고"),
                                fieldWithPath("isPacking").description("포장 여부"),
                                fieldWithPath("authorList").description("저자 목록"),
                                fieldWithPath("categoryList").description("카테고리 목록"),
                                fieldWithPath("tagList").optional().description("태그 목록")
                        ),
                        responseFields(
                                fieldWithPath("id").description("생성된 도서의 ID"),
                                fieldWithPath("name").description("생성된 도서의 이름")
                        )));
        verify(bookService, times(1)).createBook(any(BookCreateRequest.class), any(MultipartFile.class),
                anyList());
    }

    @Test
    @DisplayName("도서 등록(검증 실패)")
    void givenInValidBookCreateRequest_whenCreateBook_thenThrowBindException() throws Exception {
        BookCreateRequest request =
                new BookCreateRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, null, new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));
        BookCreateResponse response = new BookCreateResponse();
        response.setName(request.getName());

        when(bookService.createBook(any(BookCreateRequest.class), any(MultipartFile.class),
                anyList())).thenReturn(response);

        mockMvc.perform(multipart(url)
                        .file(new MockMultipartFile("request", "", "application/json",
                                objectMapper.writeValueAsString(request).getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andDo(document("book-create-error"));


        verify(bookService, times(0)).createBook(any(BookCreateRequest.class), any(MultipartFile.class),
                anyList());
    }

    @Test
    @DisplayName("도서 수정(검증 성공)")
    void givenBookIdAndValidBookModifyRequest_whenModifyBook_thenModifyBookAndReturnBookModifyResponse()
            throws Exception {
        Long bookId = 3L;
        BookModifyRequest request =
                new BookModifyRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));

        BookModifyResponse response = new BookModifyResponse();
        response.setName("도서명");

        when(bookService.modifyBook(eq(bookId), any(BookModifyRequest.class), any(MultipartFile.class),
                anyList())).thenReturn(response);

        MockMultipartFile requestFile =
                new MockMultipartFile("request", "", "application/json",
                        objectMapper.writeValueAsString(request).getBytes());
        MockMultipartFile thumbnailFile =
                new MockMultipartFile("thumbnail", "thumbnail.png", "image/png", "thumbnail".getBytes());
        MockMultipartFile contentFile =
                new MockMultipartFile("content", "content.png", "image/png", "content".getBytes());
        MockMultipartFile contentFile2 =
                new MockMultipartFile("content", "content2.png", "image/png", "content2".getBytes());


        mockMvc.perform(multipart(url + "/{id}", bookId)
                        .file(requestFile)
                        .file(thumbnailFile)
                        .file(contentFile)
                        .file(contentFile2)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andDo(document("book-modify",
                        requestParts(
                                partWithName("request").description("도서 수정 요청 본문"),
                                partWithName("thumbnail").description("도서 썸네일 이미지"),
                                partWithName("content").description("도서 내용 파일들")
                        ),
                        requestPartFields("request",
                                fieldWithPath("bookStatusId").description("도서 상태 ID"),
                                fieldWithPath("publisherId").description("출판사 ID"),
                                fieldWithPath("name").description("도서명"),
                                fieldWithPath("isbn").description("ISBN"),
                                fieldWithPath("publishDate").description("출판일"),
                                fieldWithPath("page").description("페이지 수"),
                                fieldWithPath("index").description("목차"),
                                fieldWithPath("explanation").description("도서 설명"),
                                fieldWithPath("originalCost").description("정가"),
                                fieldWithPath("saleCost").description("판매가"),
                                fieldWithPath("stock").description("재고"),
                                fieldWithPath("isPacking").description("포장 여부"),
                                fieldWithPath("authorList").description("저자 목록"),
                                fieldWithPath("categoryList").description("카테고리 목록"),
                                fieldWithPath("tagList").optional().description("태그 목록")
                        ),
                        responseFields(
                                fieldWithPath("name").description("수정된 도서의 이름")
                        )));

        verify(bookService, times(1)).modifyBook(eq(bookId), any(BookModifyRequest.class), any(MultipartFile.class),
                anyList());
    }

    @Test
    @DisplayName("도서 수정(검증 실패)")
    void givenBookIdAndInValidBookModifyRequest_whenModifyBook_thenThrowBindException() throws Exception {
        Long bookId = 3L;
        BookModifyRequest request =
                new BookModifyRequest("판매중", 1, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, new ArrayList<Integer>(List.of(1)), new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));

        BookModifyResponse response = new BookModifyResponse();
        response.setName("도서명");

        when(bookService.modifyBook(eq(bookId), any(BookModifyRequest.class), any(MultipartFile.class),
                anyList())).thenReturn(response);

        mockMvc.perform(multipart(url + "/{id}", bookId)
                        .file(new MockMultipartFile("request", "", "application/json",
                                objectMapper.writeValueAsString(request).getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isOk())
                .andDo(document("book-modify-error"));

        verify(bookService, times(0)).modifyBook(eq(bookId), any(BookModifyRequest.class), any(MultipartFile.class),
                anyList());
    }
}