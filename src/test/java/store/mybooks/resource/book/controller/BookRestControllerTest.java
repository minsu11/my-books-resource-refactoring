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
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.book.dto.request.BookCreateRequest;
import store.mybooks.resource.book.dto.request.BookModifyRequest;
import store.mybooks.resource.book.dto.response.BookBriefResponse;
import store.mybooks.resource.book.dto.response.BookCartResponse;
import store.mybooks.resource.book.dto.response.BookCreateResponse;
import store.mybooks.resource.book.dto.response.BookDetailResponse;
import store.mybooks.resource.book.dto.response.BookGetResponseForCoupon;
import store.mybooks.resource.book.dto.response.BookLikeResponse;
import store.mybooks.resource.book.dto.response.BookModifyResponse;
import store.mybooks.resource.book.dto.response.BookPopularityResponse;
import store.mybooks.resource.book.dto.response.BookPublicationDateResponse;
import store.mybooks.resource.book.dto.response.BookRatingResponse;
import store.mybooks.resource.book.dto.response.BookResponseForOrder;
import store.mybooks.resource.book.dto.response.BookReviewResponse;
import store.mybooks.resource.book.dto.response.BookStockResponse;
import store.mybooks.resource.book.service.BookService;
import store.mybooks.resource.category.dto.response.CategoryIdNameGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.tag.dto.response.TagGetResponseForBookDetail;
import store.mybooks.resource.utils.TimeUtils;

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
                                partWithName("content").description("도서 설명 이미지 파일들")
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
                new BookCreateRequest("판매중", 1, null, "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1", "내용1",
                        20000, 16000, 5, true, null, new ArrayList<Integer>(List.of(1)),
                        new ArrayList<Integer>(List.of(1)));

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
                                partWithName("content").description("도서 설명 이미지 파일들")
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

    @Test
    @DisplayName("도서 ID로 상세정보 조회")
    void givenBookId_whenGetBookDetail_thenReturnBookDetailResponse() throws Exception {
        Long bookId = 1L;
        BookDetailResponse response = BookDetailResponse.builder()
                .id(bookId)
                .thumbNailImage("thumbnailImagePath")
                .name("Book Name")
                .bookStatus("판매중")
                .authorList(List.of(new AuthorGetResponse(1, "Author Name", "Author Content")))
                .publisher(new PublisherGetResponse(1, "Publisher Name"))
                .publishDate(LocalDate.of(2024, 1, 1))
                .saleCost(15000)
                .originalCost(20000)
                .disCountRate(25)
                .rate(4.5)
                .reviewCount(100L)
                .likeCount(250)
                .isPacking(true)
                .page(300)
                .isbn("1234567890123")
                .categoryList(List.of(new CategoryIdNameGetResponse(1, "Category Name")))
                .tagList(List.of(new TagGetResponseForBookDetail(1, "TagName")))
                .stock(50)
                .index("Book Index")
                .explanation("Book Explanation")
                .contentImageList(List.of("imagePath1", "imagePath2"))
                .build();

        when(bookService.getBookDetailInfo(bookId)).thenReturn(response);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/books/{id}", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.thumbNailImage").value(response.getThumbNailImage()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.bookStatus").value(response.getBookStatus()))
                .andExpect(jsonPath("$.authorList[0].id").value(response.getAuthorList().get(0).getId()))
                .andExpect(jsonPath("$.authorList[0].name").value(response.getAuthorList().get(0).getName()))
                .andExpect(jsonPath("$.authorList[0].content").value(response.getAuthorList().get(0).getContent()))
                .andExpect(jsonPath("$.publisher.id").value(response.getPublisher().getId()))
                .andExpect(jsonPath("$.publisher.name").value(response.getPublisher().getName()))
                .andExpect(jsonPath("$.publishDate").value(response.getPublishDate().toString()))
                .andExpect(jsonPath("$.saleCost").value(response.getSaleCost()))
                .andExpect(jsonPath("$.originalCost").value(response.getOriginalCost()))
                .andExpect(jsonPath("$.disCountRate").value(response.getDisCountRate()))
                .andExpect(jsonPath("$.rate").value(response.getRate()))
                .andExpect(jsonPath("$.reviewCount").value(response.getReviewCount()))
                .andExpect(jsonPath("$.likeCount").value(response.getLikeCount()))
                .andExpect(jsonPath("$.isPacking").value(response.getIsPacking()))
                .andExpect(jsonPath("$.page").value(response.getPage()))
                .andExpect(jsonPath("$.isbn").value(response.getIsbn()))
                .andExpect(jsonPath("$.categoryList[0].id").value(response.getCategoryList().get(0).getId()))
                .andExpect(jsonPath("$.categoryList[0].name").value(response.getCategoryList().get(0).getName()))
                .andExpect(jsonPath("$.tagList[0].id").value(response.getTagList().get(0).getId()))
                .andExpect(jsonPath("$.tagList[0].name").value(response.getTagList().get(0).getName()))
                .andExpect(jsonPath("$.stock").value(response.getStock()))
                .andExpect(jsonPath("$.index").value(response.getIndex()))
                .andExpect(jsonPath("$.explanation").value(response.getExplanation()))
                .andExpect(jsonPath("$.contentImageList", Matchers.hasSize(response.getContentImageList().size())))
                .andDo(document("book-detail",
                        pathParameters(
                                parameterWithName("id").description("도서 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("도서 ID"),
                                fieldWithPath("thumbNailImage").description("도서 썸네일 이미지"),
                                fieldWithPath("name").description("도서명"),
                                fieldWithPath("bookStatus").description("도서 상태"),
                                subsectionWithPath("authorList").description("저자 목록"),
                                subsectionWithPath("publisher").description("출판사"),
                                fieldWithPath("publishDate").description("출판일"),
                                fieldWithPath("saleCost").description("판매가"),
                                fieldWithPath("originalCost").description("정가"),
                                fieldWithPath("disCountRate").description("할인율"),
                                fieldWithPath("rate").description("평점"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("likeCount").description("좋아요 수"),
                                fieldWithPath("isPacking").description("포장 여부"),
                                fieldWithPath("page").description("페이지 수"),
                                fieldWithPath("isbn").description("ISBN"),
                                subsectionWithPath("categoryList").description("카테고리 목록"),
                                subsectionWithPath("tagList").description("태그 목록"),
                                fieldWithPath("stock").description("재고"),
                                fieldWithPath("index").description("목차"),
                                fieldWithPath("explanation").description("설명"),
                                fieldWithPath("contentImageList").description("도서 설명 이미지 파일들")
                        )
                ));
        verify(bookService, times(1)).getBookDetailInfo(bookId);
    }

    @Test
    @DisplayName("관리자 페이지에서 사용할 페이징된 도서 미리보기")
    void givenPageable_whenGetBookBrief_thenReturnPagedBookBriefResponse() throws Exception {
        int page = 0, size = 2;

        BookBriefResponse response1 = new BookBriefResponse();
        response1.setId(1L);
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

        when(bookService.getBookBriefInfo(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get(url + "?page=" + page + "&size=" + size)
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
                .andDo(document("book-getBookBrief",
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].id").description("도서 ID"),
                                fieldWithPath("content[].image").description("도서 썸네일 이미지"),
                                fieldWithPath("content[].name").description("도서명"),
                                fieldWithPath("content[].rate").description("평점"),
                                fieldWithPath("content[].reviewCount").description("도서 리뷰 수"),
                                fieldWithPath("content[].cost").description("정가"),
                                fieldWithPath("content[].saleCost").description("판매가"),
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
        verify(bookService, times(1)).getBookBriefInfo(any(Pageable.class));
    }

    @Test
    @DisplayName("출판일순으로 정렬된 도서 리스트")
    void whenGetBookPublicationDate_thenReturnBookPublicationDateResponseList() throws Exception {
        List<BookPublicationDateResponse> responses = List.of(
                new BookPublicationDateResponse(1L, "image", "name", 0L, 1000, 1000, 0.0, TimeUtils.nowDate()),
                new BookPublicationDateResponse(2L, "image1", "name2", 1L, 2000, 2000, 1.0, TimeUtils.nowDate())
        );
        when(bookService.getBookPublicationDateList()).thenReturn(responses);
        mockMvc.perform(get(url + "/bookPublicationDate")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responses.get(0).getId()))
                .andExpect(jsonPath("$[0].image").value(responses.get(0).getImage()))
                .andExpect(jsonPath("$[0].name").value(responses.get(0).getName()))
                .andExpect(jsonPath("$[0].reviewCount").value(responses.get(0).getReviewCount()))
                .andExpect(jsonPath("$[0].cost").value(responses.get(0).getCost()))
                .andExpect(jsonPath("$[0].saleCost").value(responses.get(0).getSaleCost()))
                .andExpect(jsonPath("$[0].rate").value(responses.get(0).getRate()))
                .andExpect(jsonPath("$[0].publicationDate").value(responses.get(0).getPublicationDate().toString()))
                .andExpect(jsonPath("$[1].id").value(responses.get(1).getId()))
                .andExpect(jsonPath("$[1].image").value(responses.get(1).getImage()))
                .andExpect(jsonPath("$[1].name").value(responses.get(1).getName()))
                .andExpect(jsonPath("$[1].reviewCount").value(responses.get(1).getReviewCount()))
                .andExpect(jsonPath("$[1].cost").value(responses.get(1).getCost()))
                .andExpect(jsonPath("$[1].saleCost").value(responses.get(1).getSaleCost()))
                .andExpect(jsonPath("$[1].rate").value(responses.get(1).getRate()))
                .andExpect(jsonPath("$[1].publicationDate").value(responses.get(1).getPublicationDate().toString()))
                .andDo(document("book-getBookPublicationDate",
                        responseFields(
                                fieldWithPath("[].id").description("도서 ID"),
                                fieldWithPath("[].image").description("도서 이미지 경로"),
                                fieldWithPath("[].name").description("도서명"),
                                fieldWithPath("[].reviewCount").description("리뷰 수"),
                                fieldWithPath("[].cost").description("정가"),
                                fieldWithPath("[].saleCost").description("판매가"),
                                fieldWithPath("[].rate").description("평점"),
                                fieldWithPath("[].publicationDate").description("출판일")
                        )));
        verify(bookService, times(1)).getBookPublicationDateList();
    }

    @Test
    @DisplayName("평점으로 정렬된 도서 리스트")
    void whenGetBookRating_thenReturnBookRatingResponseList() throws Exception {
        List<BookRatingResponse> responses = List.of(
                new BookRatingResponse(1L, "image", "name", 0L, 1000, 1000, 0.0),
                new BookRatingResponse(2L, "image1", "name2", 1L, 2000, 2000, 1.0)
        );

        when(bookService.getBookRatingList()).thenReturn(responses);
        mockMvc.perform(get(url + "/bookRating")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responses.get(0).getId()))
                .andExpect(jsonPath("$[0].image").value(responses.get(0).getImage()))
                .andExpect(jsonPath("$[0].name").value(responses.get(0).getName()))
                .andExpect(jsonPath("$[0].reviewCount").value(responses.get(0).getReviewCount()))
                .andExpect(jsonPath("$[0].cost").value(responses.get(0).getCost()))
                .andExpect(jsonPath("$[0].saleCost").value(responses.get(0).getSaleCost()))
                .andExpect(jsonPath("$[0].rate").value(responses.get(0).getRate()))
                .andExpect(jsonPath("$[1].id").value(responses.get(1).getId()))
                .andExpect(jsonPath("$[1].image").value(responses.get(1).getImage()))
                .andExpect(jsonPath("$[1].name").value(responses.get(1).getName()))
                .andExpect(jsonPath("$[1].reviewCount").value(responses.get(1).getReviewCount()))
                .andExpect(jsonPath("$[1].cost").value(responses.get(1).getCost()))
                .andExpect(jsonPath("$[1].saleCost").value(responses.get(1).getSaleCost()))
                .andExpect(jsonPath("$[1].rate").value(responses.get(1).getRate()))
                .andDo(document("book-getBookRating",
                        responseFields(
                                fieldWithPath("[].id").description("도서 ID"),
                                fieldWithPath("[].image").description("도서 이미지 경로"),
                                fieldWithPath("[].name").description("도서명"),
                                fieldWithPath("[].reviewCount").description("리뷰 수"),
                                fieldWithPath("[].cost").description("정가"),
                                fieldWithPath("[].saleCost").description("판매가"),
                                fieldWithPath("[].rate").description("평점")
                        )));
        verify(bookService, times(1)).getBookRatingList();
    }

    @Test
    @DisplayName("리뷰수로 정렬된 도서 리스트")
    void whenGetBookReview_thenReturnBookReviewResponseList() throws Exception {
        List<BookReviewResponse> responses = List.of(
                new BookReviewResponse(1L, "image", "name", 0L, 1000, 1000, 0.0),
                new BookReviewResponse(2L, "image1", "name2", 1L, 2000, 2000, 1.0)
        );

        when(bookService.getBookReviewList()).thenReturn(responses);
        mockMvc.perform(get(url + "/bookReviewCount")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responses.get(0).getId()))
                .andExpect(jsonPath("$[0].image").value(responses.get(0).getImage()))
                .andExpect(jsonPath("$[0].name").value(responses.get(0).getName()))
                .andExpect(jsonPath("$[0].reviewCount").value(responses.get(0).getReviewCount()))
                .andExpect(jsonPath("$[0].cost").value(responses.get(0).getCost()))
                .andExpect(jsonPath("$[0].saleCost").value(responses.get(0).getSaleCost()))
                .andExpect(jsonPath("$[0].rate").value(responses.get(0).getRate()))
                .andExpect(jsonPath("$[1].id").value(responses.get(1).getId()))
                .andExpect(jsonPath("$[1].image").value(responses.get(1).getImage()))
                .andExpect(jsonPath("$[1].name").value(responses.get(1).getName()))
                .andExpect(jsonPath("$[1].reviewCount").value(responses.get(1).getReviewCount()))
                .andExpect(jsonPath("$[1].cost").value(responses.get(1).getCost()))
                .andExpect(jsonPath("$[1].saleCost").value(responses.get(1).getSaleCost()))
                .andExpect(jsonPath("$[1].rate").value(responses.get(1).getRate()))
                .andDo(document("book-getBookReview",
                        responseFields(
                                fieldWithPath("[].id").description("도서 ID"),
                                fieldWithPath("[].image").description("도서 이미지 경로"),
                                fieldWithPath("[].name").description("도서명"),
                                fieldWithPath("[].reviewCount").description("리뷰 수"),
                                fieldWithPath("[].cost").description("정가"),
                                fieldWithPath("[].saleCost").description("판매가"),
                                fieldWithPath("[].rate").description("평점")
                        )));
        verify(bookService, times(1)).getBookReviewList();
    }

    @Test
    @DisplayName("좋아요수로 정렬된 도서 리스트")
    void whenGetBookLike_thenReturnBookLikeResponseList() throws Exception {
        List<BookLikeResponse> responses = List.of(
                new BookLikeResponse(1L, "image", "name", 0L, 1000, 1000, 0.0, 1L),
                new BookLikeResponse(2L, "image1", "name2", 1L, 2000, 2000, 1.0, 1L)
        );

        when(bookService.getBookLikeList()).thenReturn(responses);
        mockMvc.perform(get(url + "/bookLike")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responses.get(0).getId()))
                .andExpect(jsonPath("$[0].image").value(responses.get(0).getImage()))
                .andExpect(jsonPath("$[0].name").value(responses.get(0).getName()))
                .andExpect(jsonPath("$[0].reviewCount").value(responses.get(0).getReviewCount()))
                .andExpect(jsonPath("$[0].cost").value(responses.get(0).getCost()))
                .andExpect(jsonPath("$[0].saleCost").value(responses.get(0).getSaleCost()))
                .andExpect(jsonPath("$[0].rate").value(responses.get(0).getRate()))
                .andExpect(jsonPath("$[0].likeCount").value(responses.get(0).getLikeCount()))
                .andExpect(jsonPath("$[1].id").value(responses.get(1).getId()))
                .andExpect(jsonPath("$[1].image").value(responses.get(1).getImage()))
                .andExpect(jsonPath("$[1].name").value(responses.get(1).getName()))
                .andExpect(jsonPath("$[1].reviewCount").value(responses.get(1).getReviewCount()))
                .andExpect(jsonPath("$[1].cost").value(responses.get(1).getCost()))
                .andExpect(jsonPath("$[1].saleCost").value(responses.get(1).getSaleCost()))
                .andExpect(jsonPath("$[1].rate").value(responses.get(1).getRate()))
                .andExpect(jsonPath("$[1].likeCount").value(responses.get(1).getLikeCount()))
                .andDo(document("book-getBookLike",
                        responseFields(
                                fieldWithPath("[].id").description("도서 ID"),
                                fieldWithPath("[].image").description("도서 이미지 경로"),
                                fieldWithPath("[].name").description("도서명"),
                                fieldWithPath("[].reviewCount").description("리뷰 수"),
                                fieldWithPath("[].cost").description("정가"),
                                fieldWithPath("[].saleCost").description("판매가"),
                                fieldWithPath("[].rate").description("평점"),
                                fieldWithPath("[].likeCount").description("좋아요 수")
                        )));
        verify(bookService, times(1)).getBookLikeList();
    }

    @Test
    @DisplayName("조횟수로 정렬된 도서 리스트")
    void whenGetBookPopularity_thenReturnBookPopularityResponseList() throws Exception {
        List<BookPopularityResponse> responses = List.of(
                new BookPopularityResponse(1L, "image", "name", 0L, 1000, 1000, 0.0, 1),
                new BookPopularityResponse(2L, "image1", "name2", 1L, 2000, 2000, 1.0, 1)
        );

        when(bookService.getBookPopularityList()).thenReturn(responses);
        mockMvc.perform(get(url + "/popularity")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responses.get(0).getId()))
                .andExpect(jsonPath("$[0].image").value(responses.get(0).getImage()))
                .andExpect(jsonPath("$[0].name").value(responses.get(0).getName()))
                .andExpect(jsonPath("$[0].reviewCount").value(responses.get(0).getReviewCount()))
                .andExpect(jsonPath("$[0].cost").value(responses.get(0).getCost()))
                .andExpect(jsonPath("$[0].saleCost").value(responses.get(0).getSaleCost()))
                .andExpect(jsonPath("$[0].rate").value(responses.get(0).getRate()))
                .andExpect(jsonPath("$[0].viewCount").value(responses.get(0).getViewCount()))
                .andExpect(jsonPath("$[1].id").value(responses.get(1).getId()))
                .andExpect(jsonPath("$[1].image").value(responses.get(1).getImage()))
                .andExpect(jsonPath("$[1].name").value(responses.get(1).getName()))
                .andExpect(jsonPath("$[1].reviewCount").value(responses.get(1).getReviewCount()))
                .andExpect(jsonPath("$[1].cost").value(responses.get(1).getCost()))
                .andExpect(jsonPath("$[1].saleCost").value(responses.get(1).getSaleCost()))
                .andExpect(jsonPath("$[1].rate").value(responses.get(1).getRate()))
                .andExpect(jsonPath("$[1].viewCount").value(responses.get(1).getViewCount()))
                .andDo(document("book-getBookPopularity",
                        responseFields(
                                fieldWithPath("[].id").description("도서 ID"),
                                fieldWithPath("[].image").description("도서 이미지 경로"),
                                fieldWithPath("[].name").description("도서명"),
                                fieldWithPath("[].reviewCount").description("리뷰 수"),
                                fieldWithPath("[].cost").description("정가"),
                                fieldWithPath("[].saleCost").description("판매가"),
                                fieldWithPath("[].rate").description("평점"),
                                fieldWithPath("[].viewCount").description("조횟수")
                        )));
        verify(bookService, times(1)).getBookPopularityList();
    }

    @Test
    @DisplayName("쿠폰 등록시 사용할 도서 정보")
    void whenGetBookForCoupon_thenReturnBookGetResponseForCouponList() throws Exception {
        List<BookGetResponseForCoupon> responses = List.of(
                new BookGetResponseForCoupon(1L, "name"),
                new BookGetResponseForCoupon(2L, "name1")
        );

        when(bookService.getBookForCoupon()).thenReturn(responses);
        mockMvc.perform(get(url + "/for-coupon")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responses.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(responses.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(responses.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(responses.get(1).getName()))
                .andDo(document("book-getBookForCoupon",
                        responseFields(
                                fieldWithPath("[].id").description("도서 ID"),
                                fieldWithPath("[].name").description("도서명")
                        )));
        verify(bookService, times(1)).getBookForCoupon();
    }

    @Test
    @DisplayName("주문시 사용할 도서 정보")
    void givenBookId_whenGetBookForOrder_thenReturnBookResponseForOrder() throws Exception {
        Long bookId = 1L;
        BookResponseForOrder response = new BookResponseForOrder(1L, "name", "image", 10000, 10000, 1, false, 1);

        when(bookService.getBookForOrder(bookId)).thenReturn(response);
        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{id}/order", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.bookImage").value(response.getBookImage()))
                .andExpect(jsonPath("$.saleCost").value(response.getSaleCost()))
                .andExpect(jsonPath("$.originalCost").value(response.getOriginalCost()))
                .andExpect(jsonPath("$.disCountRate").value(response.getDisCountRate()))
                .andExpect(jsonPath("$.isPacking").value(response.getIsPacking()))
                .andExpect(jsonPath("$.stock").value(response.getStock()))
                .andDo(document("book-getBookForOrder",
                        pathParameters(
                                parameterWithName("id").description("도서 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("도서 ID"),
                                fieldWithPath("name").description("도서명"),
                                fieldWithPath("bookImage").description("도서 썸네일 이미지"),
                                fieldWithPath("saleCost").description("판매가"),
                                fieldWithPath("originalCost").description("정가"),
                                fieldWithPath("disCountRate").description("할인율"),
                                fieldWithPath("isPacking").description("포장 여부"),
                                fieldWithPath("stock").description("재고")
                        )));
        verify(bookService, times(1)).getBookForOrder(bookId);
    }

    @Test
    @DisplayName("장바구니내 도서의 정보 조회")
    void givenBookId_whenGetBookInCart_thenReturnBookCartResponse() throws Exception {
        Long bookId = 1L;
        BookCartResponse response = new BookCartResponse(1L, "name", "image", 10000, 10000, 0, "판매중");
        when(bookService.getBookInCart(bookId)).thenReturn(response);
        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/cart-books/{id}", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.bookImage").value(response.getBookImage()))
                .andExpect(jsonPath("$.cost").value(response.getCost()))
                .andExpect(jsonPath("$.saleCost").value(response.getSaleCost()))
                .andExpect(jsonPath("$.stock").value(response.getStock()))
                .andExpect(jsonPath("$.sellingStatus").value(response.getSellingStatus()))
                .andDo(document("book-getBookInCart",
                        pathParameters(
                                parameterWithName("id").description("도서 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("도서 ID"),
                                fieldWithPath("name").description("도서명"),
                                fieldWithPath("bookImage").description("도서 썸네일 이미지"),
                                fieldWithPath("cost").description("정가"),
                                fieldWithPath("saleCost").description("판매가"),
                                fieldWithPath("stock").description("재고"),
                                fieldWithPath("sellingStatus").description("도서 상태")
                        )));

        verify(bookService, times(1)).getBookInCart(bookId);
    }

    @Test
    @DisplayName("도서 재고 조회")
    void givenBookId_whenGetBookStock_thenReturnBookStockResponse() throws Exception {
        Long bookId = 1L;
        BookStockResponse response = new BookStockResponse(1L, 1);
        when(bookService.getBookStockResponse(bookId)).thenReturn(response);
        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{id}/order/stock", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.stock").value(response.getStock()))
                .andDo(document("book-getBookStock",
                        pathParameters(
                                parameterWithName("id").description("도서 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("도서 ID"),
                                fieldWithPath("stock").description("재고")
                        )));
        verify(bookService, times(1)).getBookStockResponse(bookId);
    }
}