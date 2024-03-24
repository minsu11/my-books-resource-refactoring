package store.mybooks.resource.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.review.dto.reqeust.ReviewCreateRequest;
import store.mybooks.resource.review.dto.reqeust.ReviewModifyRequest;
import store.mybooks.resource.review.dto.response.ReviewCreateResponse;
import store.mybooks.resource.review.dto.response.ReviewModifyResponse;
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
                                fieldWithPath("rate").description("수정된 리뷰의 별점"),
                                fieldWithPath("title").description("수정된 리뷰의 제목"),
                                fieldWithPath("content").description("수정된 리뷰의 본문")
                        )));

        verify(reviewService,times(1)).modifyReview(anyLong(),anyLong(),any(ReviewModifyRequest.class),any(MultipartFile.class));

    }


    @Test
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
                .andExpect(jsonPath("$.title").exists());

    }

    @Test
    void findReview() {
    }

    @Test
    void findReviewByUserId() {
    }

    @Test
    void findReviewByBookId() {
    }

    @Test
    void findReviewRateByBookId() {
    }
}