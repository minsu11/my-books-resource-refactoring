package store.mybooks.resource.usercoupon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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
import store.mybooks.resource.usercoupon.dto.request.UserCouponCreateRequest;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForMyPage;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForOrder;
import store.mybooks.resource.usercoupon.dto.response.UserCouponGetResponseForOrderQuerydsl;
import store.mybooks.resource.usercoupon.exception.UserCouponAlreadyUsedException;
import store.mybooks.resource.usercoupon.exception.UserCouponNotExistsException;
import store.mybooks.resource.usercoupon.exception.UserCouponNotUsedException;
import store.mybooks.resource.usercoupon.service.UserCouponService;
import store.mybooks.resource.utils.TimeUtils;

/**
 * packageName    : store.mybooks.resource.user_coupon.controller
 * fileName       : UserCouponRestControllerTest
 * author         : damho-lee
 * date           : 3/8/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/24          damho-lee          최초 생성
 */
@WebMvcTest(value = UserCouponRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class UserCouponRestControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserCouponService userCouponService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("회원 쿠폰 페이지 요청")
    void givenPageableAndUserId_whenGetUserCoupons_thenReturnPageOfUserCoupon() throws Exception {
        List<UserCouponGetResponseForMyPage> userCouponList = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 2);
        UserCouponGetResponseForMyPage firstUserCoupon = new UserCouponGetResponseForMyPage(
                1L,
                "firstUserCoupon",
                "전체",
                "전체 도서",
                0,
                50,
                3000,
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );
        UserCouponGetResponseForMyPage secondUserCoupon = new UserCouponGetResponseForMyPage(
                2L,
                "secondUserCoupon",
                "전체",
                "전체 도서",
                0,
                50,
                3000,
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );
        UserCouponGetResponseForMyPage thirdUserCoupon = new UserCouponGetResponseForMyPage(
                3L,
                "thirdUserCoupon",
                "전체",
                "전체 도서",
                0,
                50,
                3000,
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );
        UserCouponGetResponseForMyPage fourthUserCoupon = new UserCouponGetResponseForMyPage(
                4L,
                "fourthUserCoupon",
                "전체",
                "전체 도서",
                0,
                50,
                3000,
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );

        userCouponList.add(firstUserCoupon);
        userCouponList.add(secondUserCoupon);
        userCouponList.add(thirdUserCoupon);
        userCouponList.add(fourthUserCoupon);
        int offset = (int) pageable.getOffset();
        Page<UserCouponGetResponseForMyPage> userCouponPage = new PageImpl<>(
                userCouponList.subList(offset, offset + pageable.getPageSize()),
                pageable,
                userCouponList.size()
        );
        when(userCouponService.getUserCoupons(anyLong(), any())).thenReturn(userCouponPage);

        mockMvc.perform(
                        get("/api/user-coupon/page")
                                .header("X-USER-ID", 1)
                                .param("page", String.valueOf(pageable.getPageNumber()))
                                .param("size", String.valueOf(pageable.getPageSize()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].userCouponId").value(thirdUserCoupon.getUserCouponId()))
                .andExpect(jsonPath("$.content[1].userCouponId").value(fourthUserCoupon.getUserCouponId()))
                .andDo(document("user-coupon-page",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content[].userCouponId").description("회원 쿠폰 아이디"),
                                fieldWithPath("content[].name").description("쿠폰 이름"),
                                fieldWithPath("content[].range").description("쿠폰 범위"),
                                fieldWithPath("content[].target").description("쿠폰 적용 대상"),
                                fieldWithPath("content[].orderMin").description("최소 주문 금액"),
                                fieldWithPath("content[].discountRateOrCost").description("할인율 / 할인금액"),
                                fieldWithPath("content[].maxDiscountCost")
                                        .description("최대 할인 금액 (정액할인쿠폰인 경우 할인 금액과 같음)"),
                                fieldWithPath("content[].isRate").description("정률할인쿠폰인지 여부"),
                                fieldWithPath("content[].startDate").description("쿠폰 사용기간(시작일)"),
                                fieldWithPath("content[].endDate").description("쿠폰 사용기간(종료일)"),
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

        verify(userCouponService, times(1)).getUserCoupons(anyLong(), any());
    }

    @Test
    @DisplayName("적용 가능한 회원 쿠폰 조회")
    void givenUserIDAndBookId_whenGetUsableUserCouponsByBookId_thenReturnListOfUserCouponGetResponseForOrder()
            throws Exception {
        List<UserCouponGetResponseForOrder> userCouponList = new ArrayList<>();
        UserCouponGetResponseForOrder firstUserCoupon =
                new UserCouponGetResponseForOrder(
                        1L,
                        "firstUserCoupon",
                        0,
                        20,
                        10000,
                        true,
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(4)
                );
        UserCouponGetResponseForOrder secondUserCoupon =
                new UserCouponGetResponseForOrder(
                        1L,
                        "secondUserCoupon",
                        0,
                        20,
                        10000,
                        true,
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(4)
                );
        UserCouponGetResponseForOrder thirdUserCoupon =
                new UserCouponGetResponseForOrder(
                        1L,
                        "thirdUserCoupon",
                        0,
                        20,
                        10000,
                        true,
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(4)
                );
        UserCouponGetResponseForOrder fourthUserCoupon =
                new UserCouponGetResponseForOrder(
                        1L,
                        "fourthUserCoupon",
                        0,
                        20,
                        10000,
                        true,
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(4)
                );
        userCouponList.add(firstUserCoupon);
        userCouponList.add(secondUserCoupon);
        userCouponList.add(thirdUserCoupon);
        userCouponList.add(fourthUserCoupon);

        when(userCouponService.getUsableUserCouponsByBookId(anyLong(), anyLong())).thenReturn(userCouponList);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/user-coupon/usable-coupon/{bookId}", 1)
                        .header("X-USER-ID", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(userCouponList.size()))
                .andExpect(jsonPath("$[0].userCouponId").value(firstUserCoupon.getUserCouponId()))
                .andExpect(jsonPath("$[1].userCouponId").value(secondUserCoupon.getUserCouponId()))
                .andExpect(jsonPath("$[2].userCouponId").value(thirdUserCoupon.getUserCouponId()))
                .andExpect(jsonPath("$[3].userCouponId").value(fourthUserCoupon.getUserCouponId()))
                .andDo(document("user-coupon-list",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        pathParameters(
                                parameterWithName("bookId").description("도서 아이디")
                        ),
                        responseFields(
                                fieldWithPath("[].userCouponId").description("회원 쿠폰 아이디"),
                                fieldWithPath("[].name").description("쿠폰 이름"),
                                fieldWithPath("[].orderMin").description("최소 주문 금액"),
                                fieldWithPath("[].discountRateOrCost").description("할인율 / 할인금액"),
                                fieldWithPath("[].maxDiscountCost").description("최대 할인 금액 (정액할인쿠폰인 경우 할인 금액과 같음)"),
                                fieldWithPath("[].rate").description("정률할인쿠폰인지 여부"),
                                fieldWithPath("[].startDate").description("쿠폰 사용기간(시작일)"),
                                fieldWithPath("[].endDate").description("쿠폰 사용기간(종료일)")
                        )
                ));
    }

    @Test
    @DisplayName("적용 가능한 전체 회원 쿠폰 조회")
    void givenUserId_whenGetUsableTotalCoupons_thenReturnListOfUserCouponGetResponseForOrder() throws Exception {
        List<UserCouponGetResponseForOrder> userCouponList = new ArrayList<>();
        UserCouponGetResponseForOrder firstUserCoupon =
                new UserCouponGetResponseForOrder(
                        1L,
                        "firstUserCoupon",
                        0,
                        20,
                        10000,
                        true,
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(4)
                );
        UserCouponGetResponseForOrder secondUserCoupon =
                new UserCouponGetResponseForOrder(
                        1L,
                        "secondUserCoupon",
                        0,
                        20,
                        10000,
                        true,
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(4)
                );
        UserCouponGetResponseForOrder thirdUserCoupon =
                new UserCouponGetResponseForOrder(
                        1L,
                        "thirdUserCoupon",
                        0,
                        20,
                        10000,
                        true,
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(4)
                );
        UserCouponGetResponseForOrder fourthUserCoupon =
                new UserCouponGetResponseForOrder(
                        1L,
                        "fourthUserCoupon",
                        0,
                        20,
                        10000,
                        true,
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(4)
                );
        userCouponList.add(firstUserCoupon);
        userCouponList.add(secondUserCoupon);
        userCouponList.add(thirdUserCoupon);
        userCouponList.add(fourthUserCoupon);

        when(userCouponService.getUsableTotalCoupons(anyLong())).thenReturn(userCouponList);

        mockMvc.perform(get("/api/user-coupon/usable-coupon")
                        .header("X-USER-ID", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(userCouponList.size()))
                .andExpect(jsonPath("$[0].userCouponId").value(firstUserCoupon.getUserCouponId()))
                .andExpect(jsonPath("$[1].userCouponId").value(secondUserCoupon.getUserCouponId()))
                .andExpect(jsonPath("$[2].userCouponId").value(thirdUserCoupon.getUserCouponId()))
                .andExpect(jsonPath("$[3].userCouponId").value(fourthUserCoupon.getUserCouponId()));
    }

    @Test
    @DisplayName("회원 쿠폰 생성")
    void givenUserCouponCreateRequest_whenCreateUserCoupon_thenCreateUserCoupon() throws Exception {
        UserCouponCreateRequest userCouponCreateRequest = new UserCouponCreateRequest();
        ReflectionTestUtils.setField(userCouponCreateRequest, "userId", 1L);
        ReflectionTestUtils.setField(userCouponCreateRequest, "couponId", 1L);
        String content = objectMapper.writeValueAsString(userCouponCreateRequest);
        doNothing().when(userCouponService).createUserCoupon(any());

        mockMvc.perform(post("/api/user-coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andDo(document("user-coupon-create",
                        requestFields(
                                fieldWithPath("userId").description("회원 아이디"),
                                fieldWithPath("couponId").description("쿠폰 아이디")
                        )));

        verify(userCouponService, times(1)).createUserCoupon(any());
    }

    @Test
    @DisplayName("회원 쿠폰 생성 - Validation 실패(userId)")
    void givenWrongUserCouponCreateRequest_whenCreateUserCoupon_thenThrowRequestValidationFailedException()
            throws Exception {
        UserCouponCreateRequest userCouponCreateRequest = new UserCouponCreateRequest();
        ReflectionTestUtils.setField(userCouponCreateRequest, "userId", -1L);
        ReflectionTestUtils.setField(userCouponCreateRequest, "couponId", 1L);
        String userIdNegative = objectMapper.writeValueAsString(userCouponCreateRequest);

        ReflectionTestUtils.setField(userCouponCreateRequest, "userId", null);
        String userIdNull = objectMapper.writeValueAsString(userCouponCreateRequest);

        mockMvc.perform(post("/api/user-coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userIdNegative))
                .andExpect(status().isBadRequest())
                .andDo(document("user_coupon-create-fail-validation-userIdNegative"));

        mockMvc.perform(post("/api/user-coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userIdNull))
                .andExpect(status().isBadRequest())
                .andDo(document("user_coupon-create-fail-validation-userIdNull"));

        verify(userCouponService, times(0)).createUserCoupon(any());
    }

    @Test
    @DisplayName("회원 쿠폰 생성 실패 - Validation 실패(couponId)")
    void givenInvalidUserId_whenCreateUserCoupon_thenThrowValidationException() throws Exception {
        UserCouponCreateRequest userCouponCreateRequest = new UserCouponCreateRequest();
        ReflectionTestUtils.setField(userCouponCreateRequest, "userId", 1L);
        ReflectionTestUtils.setField(userCouponCreateRequest, "couponId", -1L);
        String couponIdNegative = objectMapper.writeValueAsString(userCouponCreateRequest);
        ReflectionTestUtils.setField(userCouponCreateRequest, "couponId", null);
        String couponIdNull = objectMapper.writeValueAsString(userCouponCreateRequest);

        mockMvc.perform(post("/api/user-coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(couponIdNegative))
                .andExpect(status().isBadRequest())
                .andDo(document("user_coupon-create-fail-validation-couponIdNegative"));

        mockMvc.perform(post("/api/user-coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(couponIdNull))
                .andExpect(status().isBadRequest())
                .andDo(document("user_coupon-create-fail-validation-couponIdNull"));
    }

    @Test
    @DisplayName("회원 쿠폰 사용")
    void givenUserCouponId_whenUseUserCoupon_thenModifyUserCoupon() throws Exception {
        doNothing().when(userCouponService).useUserCoupon(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/user-coupon/use/{userCouponId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("user-coupon-use",
                        pathParameters(
                                parameterWithName("userCouponId").description("회원 쿠폰 아이디")
                        )));

        verify(userCouponService, times(1)).useUserCoupon(anyLong());
    }

    @Test
    @DisplayName("회원 쿠폰 사용 - 이미 사용한 쿠폰")
    void givenAlreadyUsedUserCouponId_whenUseUserCoupon_thenModifyUserCoupon() throws Exception {
        doThrow(new UserCouponAlreadyUsedException(1L)).when(userCouponService).useUserCoupon(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/user-coupon/use/{userCouponId}", 1L))
                .andExpect(status().isNotFound())
                .andDo(document("user_coupon-use-fail-alreadyUsedCoupon",
                        pathParameters(
                                parameterWithName("userCouponId").description("회원 쿠폰 아이디")
                        )));

        verify(userCouponService, times(1)).useUserCoupon(anyLong());
    }

    @Test
    @DisplayName("회원 쿠폰 돌려줌")
    void givenUserCouponId_whenGiveBackUserCoupon_thenModifyUserCoupon() throws Exception {
        doNothing().when(userCouponService).giveBackUserCoupon(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/user-coupon/return/{userCouponId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("user-coupon-use",
                        pathParameters(
                                parameterWithName("userCouponId").description("회원 쿠폰 아이디")
                        )));

        verify(userCouponService, times(1)).giveBackUserCoupon(anyLong());
    }

    @Test
    @DisplayName("회원 쿠폰 돌려줌 - 사용하지 않은 쿠폰")
    void givenNotUsedUserCouponId_whenUseUserCoupon_thenModifyUserCoupon() throws Exception {
        doThrow(new UserCouponNotUsedException(1L)).when(userCouponService).giveBackUserCoupon(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/user-coupon/return/{userCouponId}", 1L))
                .andExpect(status().isNotFound())
                .andDo(document("user_coupon-use-fail-notUsedCoupon",
                        pathParameters(
                                parameterWithName("userCouponId").description("회원 쿠폰 아이디")
                        )));

        verify(userCouponService, times(1)).giveBackUserCoupon(anyLong());
    }

    @Test
    @DisplayName("회원 쿠폰 조회")
    void givenUserCouponId_whenGetUserCoupon_thenReturnUserCouponGetResponseForOrderQuerydsl() throws Exception {
        UserCouponGetResponseForOrderQuerydsl expect =
                new UserCouponGetResponseForOrderQuerydsl(
                        1L,
                        "userCoupon",
                        0,
                        null,
                        3000,
                        50,
                        true,
                        TimeUtils.nowDate().minusDays(3),
                        TimeUtils.nowDate().plusDays(3)
                );
        when(userCouponService.getUserCoupon(anyLong())).thenReturn(expect);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/user-coupon/{userCouponId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userCouponId").value(expect.getUserCouponId()))
                .andExpect(jsonPath("$.name").value(expect.getName()))
                .andExpect(jsonPath("$.orderMin").value(expect.getOrderMin()))
                .andExpect(jsonPath("$.discountCost").value(expect.getDiscountCost()))
                .andExpect(jsonPath("$.maxDiscountCost").value(expect.getMaxDiscountCost()))
                .andExpect(jsonPath("$.discountRate").value(expect.getDiscountRate()))
                .andExpect(jsonPath("$.rate").value(expect.isRate()))
                .andExpect(jsonPath("$.startDate").value(expect.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(expect.getEndDate().toString()))
                .andDo(document("user-coupon-get",
                        pathParameters(
                                parameterWithName("userCouponId").description("회원 쿠폰 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userCouponId").description("회원 쿠폰 아이디"),
                                fieldWithPath("name").description("쿠폰 이름"),
                                fieldWithPath("orderMin").description("최소 주문 금액"),
                                fieldWithPath("discountCost").description("할인 금액"),
                                fieldWithPath("maxDiscountCost").description("최대 할인 금액"),
                                fieldWithPath("discountRate").description("할인율"),
                                fieldWithPath("rate").description("정률 할인 쿠폰인지 여부"),
                                fieldWithPath("startDate").description("쿠폰 사용 시작일"),
                                fieldWithPath("endDate").description("쿠폰 사용 종료일")
                        )));
    }

    @Test
    @DisplayName("회원 쿠폰 조회 - 없는 아이디인 경우")
    void givenNotExistsUserCouponId_whenGetUserCoupon_thenThrowUserCouponNotExistsException() throws Exception {
        when(userCouponService.getUserCoupon(anyLong())).thenThrow(UserCouponNotExistsException.class);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/user-coupon/{userCouponId}", 1))
                .andExpect(status().isNotFound())
                .andDo(document("user-coupon-get-exception",
                        pathParameters(
                                parameterWithName("userCouponId").description("회원 쿠폰 아이디")
                        )));
    }
}