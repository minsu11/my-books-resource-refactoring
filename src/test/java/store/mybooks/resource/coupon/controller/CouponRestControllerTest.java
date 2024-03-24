package store.mybooks.resource.coupon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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
import store.mybooks.resource.coupon.exception.CouponCannotDeleteException;
import store.mybooks.resource.coupon.exception.CouponDateIncorrectException;
import store.mybooks.resource.coupon.exception.CouponInCompatibleType;
import store.mybooks.resource.coupon.exception.CouponNotExistsException;
import store.mybooks.resource.coupon.exception.OrderMinLessThanDiscountCostException;
import store.mybooks.resource.coupon.service.CouponService;
import store.mybooks.resource.utils.TimeUtils;

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

    CouponCreateRequest couponCreateRequest;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris(), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

        couponCreateRequest = new CouponCreateRequest();
        ReflectionTestUtils.setField(couponCreateRequest, "name", "firstCoupon");
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", null);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", null);
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", 3000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", LocalDate.now().plusDays(2));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", LocalDate.now().plusDays(3));
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

        mockMvc.perform(get("/api/coupons/page?page=" + pageable.getPageNumber()
                        + "&size=" + pageable.getPageSize()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(pageable.getPageSize()))
                .andExpect(jsonPath("$.content[0].id").value(thirdCoupon.getId()))
                .andExpect(jsonPath("$.content[1].id").value(fourthCoupon.getId()))
                .andDo(document("coupon-get-page",
                        requestParameters(
                                parameterWithName("page").description("요청 페이지 번호(0부터 시작, default = 0)"),
                                parameterWithName("size").description("페이지 사이즈(default = 10)")
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].id").description("쿠폰 아이디"),
                                fieldWithPath("content[].name").description("쿠폰 이름"),
                                fieldWithPath("content[].range").description("쿠폰 적용 범위"),
                                fieldWithPath("content[].target").description("쿠폰 적용 대상"),
                                fieldWithPath("content[].orderMin").description("최소 주문 금액"),
                                fieldWithPath("content[].discountRateOrCost").description(
                                        "쿠폰 할인률 또는 할인금액 (isRate 로 판단)"),
                                fieldWithPath("content[].maxDiscountCost").description(
                                        "최대 할인 금액 (정액할인쿠폰인 경우 discountCost 와 값이 같음)"),
                                fieldWithPath("content[].isRate").description("정률 할인 쿠폰인지 판단"),
                                fieldWithPath("content[].startDate").description("쿠폰 유효기간(시작일)"),
                                fieldWithPath("content[].endDate").description("쿠폰 유효기간(종료일)"),
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

        verify(couponService, times(1)).getCoupons(any());
    }

    @Test
    @DisplayName("쿠폰 생성")
    void givenCouponCreateRequest_whenCouponCreate_thenCreateCoupon() throws Exception {
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
    @DisplayName("쿠폰 생성 실패 - Validation(name)")
    void givenInvalidNameCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "name", null);
        String nullName = objectMapper.writeValueAsString(couponCreateRequest);
        ReflectionTestUtils.setField(couponCreateRequest, "name", "");
        String emptyName = objectMapper.writeValueAsString(couponCreateRequest);
        ReflectionTestUtils.setField(couponCreateRequest, "name",
                "tooLongNametooLongNametooLongNametooLongNametooLongNametooLongNametooLongNametooLongName" +
                        "tooLongNametooLongNametooLongNametooLongNametooLongNametooLongNametooLongNametooLongName" +
                        "tooLongNametooLongNametooLongNametooLongName");
        String longName = objectMapper.writeValueAsString(couponCreateRequest);

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nullName))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-nullName"));

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyName))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-emptyName"));

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(longName))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-tooLongName"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation(bookId)")
    void givenInvalidBookIdCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException() throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", 0L);
        String bookIdZero = objectMapper.writeValueAsString(couponCreateRequest);
        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookIdZero))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-bookIdZero"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation(categoryId)")
    void givenInvalidCategoryIdCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", 0);
        String categoryIdZero = objectMapper.writeValueAsString(couponCreateRequest);
        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryIdZero))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-categoryIdZero"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation(orderMin)")
    void givenInvalidOrderMinCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", null);
        String orderMinNull = objectMapper.writeValueAsString(couponCreateRequest);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", -1);
        String orderMinNegative = objectMapper.writeValueAsString(couponCreateRequest);

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderMinNull))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-orderMinNull"));
        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderMinNegative))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-orderMinNegative"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation(discountCost)")
    void givenInvalidDiscountCostCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", 0);
        String discountCostZero = objectMapper.writeValueAsString(couponCreateRequest);

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(discountCostZero))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-discountCostZero"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation(maxDiscountCost)")
    void givenInvalidMaxDiscountCostCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "maxDiscountCost", 0);
        String maxDiscountCostZero = objectMapper.writeValueAsString(couponCreateRequest);

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(maxDiscountCostZero))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-maxDiscountCostZero"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation(discountRate)")
    void givenInvalidDiscountRateCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 0);
        String discountRateZero = objectMapper.writeValueAsString(couponCreateRequest);

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(discountRateZero))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-discountRate"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation(startDate)")
    void givenInvalidStartDateCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", TimeUtils.nowDate().minusDays(3));
        String startDatePast = objectMapper.writeValueAsString(couponCreateRequest);
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", null);
        String startDateNull = objectMapper.writeValueAsString(couponCreateRequest);

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(startDatePast))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-startDatePast"));
        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(startDateNull))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-startDateNull"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation(endDate)")
    void givenInvalidEndDateCouponCreateRequest_whenCreateCoupon_thenThrowValidationFailedException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", TimeUtils.nowDate().minusDays(3));
        String endDatePast = objectMapper.writeValueAsString(couponCreateRequest);
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", null);
        String endDateNull = objectMapper.writeValueAsString(couponCreateRequest);

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(endDatePast))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-endDatePast"));
        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(endDateNull))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-validation-endDateNull"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - CouponUtilsValidation 쿠폰 시작일이 종료일보다 나중인 경우")
    void givenStartDateIsLaterThanEndDateCoupon_whenCreateCoupon_thenThrowCouponDateIncorrectException()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "startDate", TimeUtils.nowDate().plusDays(10));
        ReflectionTestUtils.setField(couponCreateRequest, "endDate", TimeUtils.nowDate().plusDays(7));
        String startDateLaterThanEndDate = objectMapper.writeValueAsString(couponCreateRequest);

        doThrow(new CouponDateIncorrectException(couponCreateRequest.getStartDate(), couponCreateRequest.getEndDate()))
                .when(couponService).createCoupon(any());

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(startDateLaterThanEndDate))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-couponUtils-validation-fail-startDateLaterThanEndDate"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - CouponUtilsValidation 도서 아이디, 카테고리 아이디 둘 다 설정된 경우")
    void givenBothBookIdAndCategoryIdIsSelectedCoupon_whenCreateCoupon_thenThrowCouponInCompatibleType()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "bookId", 1L);
        ReflectionTestUtils.setField(couponCreateRequest, "categoryId", 1);
        String invalidTypeCoupon = objectMapper.writeValueAsString(couponCreateRequest);

        doThrow(new CouponInCompatibleType()).when(couponService).createCoupon(any());

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTypeCoupon))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-couponUtils-validation-fail-bookIdAndCategoryIdSelected"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - CouponUtilsValidation 쿠폰 대상이 전체, 도서, 카테고리 둘 다 아닌 경우")
    void givenBothDiscountCostAndDiscountRateIsSelected_whenCreateCoupon_thenThrowCouponInCompatibleType()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", 20);
        String invalidTypeCoupon = objectMapper.writeValueAsString(couponCreateRequest);

        doThrow(new CouponInCompatibleType()).when(couponService).createCoupon(any());

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTypeCoupon))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-couponUtils-validation-fail-discountCostAndDiscountRateSelected"));
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - CouponUtilsValidation 최소 주문 금액이 할인 금액보다 작은 경우")
    void givenOrderMinSmallerThanDiscountCost_whenCreateCoupon_thenThrowCouponInCompatibleType()
            throws Exception {
        ReflectionTestUtils.setField(couponCreateRequest, "discountRate", null);
        ReflectionTestUtils.setField(couponCreateRequest, "discountCost", 10000);
        ReflectionTestUtils.setField(couponCreateRequest, "orderMin", 5000);
        String invalidTypeCoupon = objectMapper.writeValueAsString(couponCreateRequest);

        doThrow(new OrderMinLessThanDiscountCostException(
                couponCreateRequest.getOrderMin(), couponCreateRequest.getDiscountCost()))
                .when(couponService).createCoupon(any());

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTypeCoupon))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-create-fail-couponUtils-validation-fail-orderMinSmallerThanDiscountCost"));
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

    @Test
    @DisplayName("쿠폰 삭제 실패 - 존재하지 않는 쿠폰")
    void givenNotExistsCouponId_whenDeleteCoupon_thenThrowCouponNotExistsException() throws Exception {
        doThrow(new CouponNotExistsException(1L)).when(couponService).deleteCoupon(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/coupons/{couponId}", 1))
                .andExpect(status().isNotFound())
                .andDo(document("coupon-delete-fail-notExistsCouponId",
                        pathParameters(
                                parameterWithName("couponId").description("삭제하려는 쿠폰 아이디")
                        )));
    }

    @Test
    @DisplayName("쿠폰 삭제 실패 - 이미 나눠준 쿠폰인 경우")
    void givenAlreadyDistributedCouponId_whenDeleteCoupon_thenThrowCouponCannotDeleteException() throws Exception {
        doThrow(new CouponCannotDeleteException(1L)).when(couponService).deleteCoupon(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/coupons/{couponId}", 1))
                .andExpect(status().isBadRequest())
                .andDo(document("coupon-delete-fail-alreadyDistributedCouponId",
                        pathParameters(
                                parameterWithName("couponId").description("삭제하려는 쿠폰 아이디")
                        )));
    }
}