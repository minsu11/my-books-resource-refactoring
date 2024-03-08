package store.mybooks.resource.coupon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.coupon.dto.request.CouponCreateRequest;
import store.mybooks.resource.coupon.dto.response.CouponGetResponse;
import store.mybooks.resource.coupon.service.CouponService;

/**
 * packageName    : store.mybooks.resource.coupon.controller
 * fileName       : CouponRestControllerTest
 * author         : damho-lee
 * date           : 3/8/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/24          damho-lee          최초 생성
 */
@WebMvcTest(value = CouponRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class CouponRestControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CouponService couponService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("쿠폰 페이지 요청")
    void givenPageable_whenGetCoupons_thenReturnPageOfCoupon() throws Exception {
        List<CouponGetResponse> couponList = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 2);
        CouponGetResponse firstCoupon =
                new CouponGetResponse(
                        1L,
                        "firstCoupon",
                        "전체",
                        "전체 도서",
                        0,
                        40,
                        10000,
                        true,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2)
                );
        CouponGetResponse secondCoupon =
                new CouponGetResponse(
                        2L,
                        "secondCoupon",
                        "전체",
                        "전체 도서",
                        0,
                        40,
                        10000,
                        true,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2)
                );
        CouponGetResponse thirdCoupon =
                new CouponGetResponse(
                        3L,
                        "thirdCoupon",
                        "전체",
                        "전체 도서",
                        0,
                        40,
                        10000,
                        true,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2)
                );
        CouponGetResponse fourthCoupon =
                new CouponGetResponse(
                        4L,
                        "fourthCoupon",
                        "전체",
                        "전체 도서",
                        0,
                        40,
                        10000,
                        true,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(2)
                );
        couponList.add(firstCoupon);
        couponList.add(secondCoupon);
        couponList.add(thirdCoupon);
        couponList.add(fourthCoupon);
        int offset = (int) pageable.getOffset();
        Page<CouponGetResponse> couponPage =
                new PageImpl<>(couponList.subList(offset, offset + pageable.getPageSize()), pageable,
                        couponList.size());
        when(couponService.getCoupons(any())).thenReturn(couponPage);
        String content = objectMapper.writeValueAsString(pageable);

        mockMvc.perform(get("/api/coupons/page")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(pageable.getPageSize()))
                .andExpect(jsonPath("$.content[0].id").value(thirdCoupon.getId()))
                .andExpect(jsonPath("$.content[1].id").value(fourthCoupon.getId()))
                .andDo(document("coupon-get-page",
                        requestFields(
                                fieldWithPath("pageNumber").description("페이지"),
                                fieldWithPath("pageSize").description("사이즈"),
                                fieldWithPath("sort.*").ignored(),
                                fieldWithPath("offset").ignored(),
                                fieldWithPath("paged").ignored(),
                                fieldWithPath("unpaged").ignored()
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].id").description("쿠폰 아이디"),
                                fieldWithPath("content[].name").description("쿠폰 이름"),
                                fieldWithPath("content[].range").description("쿠폰 적용 범위"),
                                fieldWithPath("content[].target").description("쿠폰 적용 대상"),
                                fieldWithPath("content[].orderMin").description("최소 주문 금액"),
                                fieldWithPath("content[].discountRateOrCost").description("쿠폰 할인률 또는 할인금액 (isRate 로 판단)"),
                                fieldWithPath("content[].maxDiscountCost").description("최대 할인 금액 (정액할인쿠폰인 경우 discountCost 와 값이 같음)"),
                                fieldWithPath("content[].isRate").description("정률 할인 쿠폰인지 판단"),
                                fieldWithPath("content[].startDate").description("쿠폰 유효기간(시작일)"),
                                fieldWithPath("content[].endDate").description("쿠폰 유효기간(종료일)"),
                                fieldWithPath("pageable.sort.*").ignored(),
                                fieldWithPath("pageable.*").ignored(),
                                fieldWithPath("totalElements").ignored(),
                                fieldWithPath("totalPages").ignored(),
                                fieldWithPath("last").ignored(),
                                fieldWithPath("numberOfElements").ignored(),
                                fieldWithPath("size").ignored(),
                                fieldWithPath("number").ignored(),
                                fieldWithPath("first").ignored(),
                                fieldWithPath("sort.*").ignored(),
                                fieldWithPath("empty").ignored()
                        )));

        verify(couponService, times(1)).getCoupons(any());
    }

    @Test
    @DisplayName("쿠폰 생성")
    void givenCouponCreateRequest_whenCouponCreate_thenCreateCoupon() throws Exception {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", 3000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().plusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().plusDays(3));

        doNothing().when(couponService).createCoupon(any());

        String content = objectMapper.writeValueAsString(couponCreateRequest);

        mockMvc.perform(post("/api/coupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated())
                .andDo(document("coupon-create",
                        requestFields(
                                fieldWithPath("name").description("쿠폰 이름"),
                                fieldWithPath("bookId").description("도서 아이디"),
                                fieldWithPath("categoryId").description("카테고리 아이디"),
                                fieldWithPath("orderMin").description("최소 주문 금액"),
                                fieldWithPath("discountCost").description("할인 금액"),
                                fieldWithPath("maxDiscountCost").description("최대 할인 금액"),
                                fieldWithPath("discountRate").description("쿠폰 할인율"),
                                fieldWithPath("startDate").description("시작일"),
                                fieldWithPath("endDate").description("종료일")
                        )));

        verify(couponService, times(1)).createCoupon(any());
    }

    @Test
    @DisplayName("쿠폰 삭제")
    void givenCouponId_whenDeleteCoupon_thenDeleteCoupon() throws Exception {
        doNothing().when(couponService).deleteCoupon(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/coupons/{couponId}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("coupon-delete",
                        pathParameters(
                                parameterWithName("couponId").description("삭제하려는 쿠폰 아이디")
                        )));

        verify(couponService, times(1)).deleteCoupon(anyLong());
    }
}