package store.mybooks.resource.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
import java.util.List;
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
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.review.dto.reqeust.ReviewCreateRequest;
import store.mybooks.resource.review.dto.reqeust.ReviewModifyRequest;
import store.mybooks.resource.review.dto.response.ReviewCreateResponse;
import store.mybooks.resource.review.dto.response.ReviewDetailGetResponse;
import store.mybooks.resource.review.dto.response.ReviewGetResponse;
import store.mybooks.resource.review.dto.response.ReviewModifyResponse;
import store.mybooks.resource.review.dto.response.ReviewRateResponse;
import store.mybooks.resource.review.service.ReviewService;

/**
 * packageName    : store.mybooks.resource.review.controller<br>
 * fileName       : ReviewRestControllerTest<br>
 * author         : masiljangajji<br>
 * date           : 3/24/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/24/24        masiljangajji       최초 생성
 */

@WebMvcTest(value = ReviewRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class ReviewRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewService reviewService;

    @MockBean
    PointHistoryService pointHistoryService;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris(), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }


    @Test
    @DisplayName("리뷰 수정")
    void givenReviewModifyRequest_whenCallModifyReview_thenReturnReviewModifyResponse() throws Exception {

        ReviewModifyRequest request = new ReviewModifyRequest(3, "title", "content");

        MockMultipartFile imageFile =
                new MockMultipartFile("contentImage", "filename.jpg", "image/jpeg", "image".getBytes());

        MockMultipartFile requestFile =
                new MockMultipartFile("request", "", "application/json",
                        objectMapper.writeValueAsString(request).getBytes());

        ReviewModifyResponse response = new ReviewModifyResponse(3, "title", "content");


        when(reviewService.modifyReview(anyLong(), anyLong(), any(ReviewModifyRequest.class),
                any(MultipartFile.class))).thenReturn(response);

        mockMvc.perform(multipart("/api/reviews/{reviewId}", 1)
                        .file(requestFile)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HeaderProperties.USER_ID, 1L)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists())
                .andDo(document("review-modify",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestParts(
                                partWithName("request").description("리뷰 수정 요청"),
                                partWithName("contentImage").description("리뷰 이미지 파일")
                        ),
                        requestPartFields("request",
                                fieldWithPath("rate").description("별점"),
                                fieldWithPath("title").description("리뷰 제목"),
                                fieldWithPath("content").description("리뷰 본문")
                        ),
                        responseFields(
                                fieldWithPath("rate").description("별점"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("본문")
                        )));

        verify(reviewService, times(1)).modifyReview(anyLong(), anyLong(), any(ReviewModifyRequest.class),
                any(MultipartFile.class));

    }


    @Test
    @DisplayName("리뷰 등록")
    void givenReviewCreateRequest_whenCallCreateReview_thenReturnReviewCreateResponse() throws Exception {


        ReviewCreateRequest request = new ReviewCreateRequest(1L, 1L, 5, "title", "content");

        MockMultipartFile imageFile =
                new MockMultipartFile("contentImage", "filename.jpg", "image/jpeg", "image".getBytes());

        MockMultipartFile requestFile =
                new MockMultipartFile("request", "", "application/json",
                        objectMapper.writeValueAsString(request).getBytes());

        ReviewCreateResponse response = new ReviewCreateResponse("title");


        when(reviewService.createReview(any(ReviewCreateRequest.class), anyLong(),
                any(MultipartFile.class))).thenReturn(response);

        mockMvc.perform(multipart("/api/reviews")
                        .file(requestFile)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HeaderProperties.USER_ID, 1L)
                        .with(req -> {
                            req.setMethod("POST");
                            return req;
                        }))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists())
                .andDo(document("review-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestParts(
                                partWithName("request").description("리뷰 수정 요청"),
                                partWithName("contentImage").description("리뷰 이미지 파일")
                        ),
                        requestPartFields("request",
                                fieldWithPath("orderDetailId").description("주문상세 아이디"),
                                fieldWithPath("orderId").description("주문아이디"),
                                fieldWithPath("rate").description("별점"),
                                fieldWithPath("title").description("리뷰 제목"),
                                fieldWithPath("content").description("리뷰 본문")
                        ),
                        responseFields(
                                fieldWithPath("title").description("수정된 리뷰의 제목")
                        )));

    }

    @Test
    @DisplayName("특정 리뷰정보 가져오기")
    void givenReviewIdAndUserId_whenCallFindReview_thenReturnReviewGetResponse() throws Exception {

        ReviewGetResponse reviewGetResponse =
                new ReviewGetResponse(1L, "bookName", 1L, "userName", 5, LocalDate.of(1999, 12, 17), "title", "content",
                        "reviewImageFile");

        when(reviewService.findReview(anyLong(), anyLong())).thenReturn(reviewGetResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/reviews/{reviewId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").exists())
                .andExpect(jsonPath("$.bookName").exists())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.userName").exists())
                .andExpect(jsonPath("$.rate").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.reviewImage").exists())
                .andDo(document("review-findUser",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 아이디")
                        ),
                        responseFields(
                                fieldWithPath("bookId").description("책 아이디"),
                                fieldWithPath("bookName").description("책 이름"),
                                fieldWithPath("reviewId").description("리뷰 아이디"),
                                fieldWithPath("userName").description("유저 이름"),
                                fieldWithPath("rate").description("별점"),
                                fieldWithPath("date").description("생성일"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("본문"),
                                fieldWithPath("reviewImage").description("리뷰 이미지")
                        )
                ));

    }

    @Test
    @DisplayName("유저가 작성한 모든 리뷰 가져오기")
    void givenUserId_whenCallFindReviewByUserId_thenReturnReviewGetResponsePage() throws Exception {

        Pageable pageable = PageRequest.of(0, 2);

        ReviewGetResponse reviewGetResponse1 =
                new ReviewGetResponse(1L, "bookName1", 1L, "userName1", 5, LocalDate.of(1999, 12, 17), "title1",
                        "content1", "reviewImageFile1");
        ReviewGetResponse reviewGetResponse2 =
                new ReviewGetResponse(2L, "bookName2", 2L, "userName2", 5, LocalDate.of(1999, 12, 17), "title2",
                        "content2", "reviewImageFile2");

        List<ReviewGetResponse> reviewList = List.of(reviewGetResponse1, reviewGetResponse2);
        Page<ReviewGetResponse> page = new PageImpl<>(reviewList, pageable, 120L);
        when(reviewService.findReviewByUserId(anyLong(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(
                                "/api/reviews?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(reviewList.size()))
                .andExpect(jsonPath("$.content[0].bookId").value(reviewGetResponse1.getBookId()))
                .andExpect(jsonPath("$.content[0].bookName").value(reviewGetResponse1.getBookName()))
                .andExpect(jsonPath("$.content[0].reviewId").value(reviewGetResponse1.getReviewId()))
                .andExpect(jsonPath("$.content[0].userName").value(reviewGetResponse1.getUserName()))
                .andExpect(jsonPath("$.content[0].rate").value(reviewGetResponse1.getRate()))
                .andExpect(jsonPath("$.content[0].title").value(reviewGetResponse1.getTitle()))
                .andExpect(jsonPath("$.content[0].content").value(reviewGetResponse1.getContent()))
                .andExpect(jsonPath("$.content[0].reviewImage").value(reviewGetResponse1.getReviewImage()))
                .andDo(document("review-findUserAll",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestParameters(
                                parameterWithName("page").description("요청 페이지 번호(0부터 시작, default = 0)"),
                                parameterWithName("size").description("페이지 사이즈(default = 10)")
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].bookId").description("책 아이디"),
                                fieldWithPath("content[].bookName").description("책 이름"),
                                fieldWithPath("content[].reviewId").description("리뷰 아이디"),
                                fieldWithPath("content[].userName").description("유저 이름"),
                                fieldWithPath("content[].rate").description("별점"),
                                fieldWithPath("content[].date").description("생성일"),
                                fieldWithPath("content[].title").description("제목"),
                                fieldWithPath("content[].content").description("본문"),
                                fieldWithPath("content[].reviewImage").description("리뷰 이미지"),
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
    }

    @Test
    @DisplayName("책의 모든 리뷰")
    void givenBookId_whenCallFindReviewBookId_thenReturnReviewDetailGetResponsePage() throws Exception {

        Pageable pageable = PageRequest.of(0, 2);
        ReviewDetailGetResponse reviewGetResponse1 =
                new ReviewDetailGetResponse(1L, "userName", 5, LocalDate.of(1999, 12, 17), "title", "content",
                        "reviewImage");
        ReviewDetailGetResponse reviewGetResponse2 =
                new ReviewDetailGetResponse(2L, "userName2", 5, LocalDate.of(1999, 12, 17), "title2", "content2",
                        "reviewImage2");

        List<ReviewDetailGetResponse> list = List.of(reviewGetResponse1, reviewGetResponse2);
        Page<ReviewDetailGetResponse> page = new PageImpl<>(list, pageable, 120L);

        when(reviewService.findReviewByBookId(anyLong(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(RestDocumentationRequestBuilders.get(
                        "/api/reviews/book/{bookId}?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize(),1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(list.size()))
                .andExpect(jsonPath("$.content[0].reviewId").value(reviewGetResponse1.getReviewId()))
                .andExpect(jsonPath("$.content[0].userName").value(reviewGetResponse1.getUserName()))
                .andExpect(jsonPath("$.content[0].rate").value(reviewGetResponse1.getRate()))
                .andExpect(jsonPath("$.content[0].title").value(reviewGetResponse1.getTitle()))
                .andExpect(jsonPath("$.content[0].content").value(reviewGetResponse1.getContent()))
                .andExpect(jsonPath("$.content[0].reviewImage").value(reviewGetResponse1.getReviewImage()))
                .andDo(document("review-findUserAll",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        pathParameters(
                                parameterWithName("bookId").description("책 아이디")
                        ),
                        requestParameters(
                                parameterWithName("page").description("요청 페이지 번호(0부터 시작, default = 0)"),
                                parameterWithName("size").description("페이지 사이즈(default = 10)")
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].reviewId").description("리뷰 아이디"),
                                fieldWithPath("content[].userName").description("유저 이름"),
                                fieldWithPath("content[].rate").description("별점"),
                                fieldWithPath("content[].date").description("생성일"),
                                fieldWithPath("content[].title").description("제목"),
                                fieldWithPath("content[].content").description("본문"),
                                fieldWithPath("content[].reviewImage").description("리뷰 이미지"),
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

    }

    @Test
    @DisplayName("책의 리뷰 평균 및 개수 ")
    void givenBookId_whenCallFindReviewRateByBookId_thenReturnReviewRateResponse() throws Exception {

        ReviewRateResponse response= new ReviewRateResponse(100L,4.4);

        when(reviewService.findReviewRateByBookId(anyLong())).thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/reviews/book/{bookId}/rate", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").exists())
                .andExpect(jsonPath("$.averageRate").exists())
                .andDo(document("review-findRate",
                        pathParameters(
                                parameterWithName("bookId").description("책 아이디")
                        ),
                        responseFields(
                                fieldWithPath("totalCount").description("전체 리뷰 수"),
                                fieldWithPath("averageRate").description("별점 평균")
                        )
                ));


    }
}