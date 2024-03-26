package store.mybooks.resource.bookorder.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.bookorder.dto.request.BookInfoRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderAdminModifyRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderCreateRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderInfoRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderRegisterInvoiceRequest;
import store.mybooks.resource.bookorder.dto.response.BookOrderCreateResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderPaymentInfoRespones;
import store.mybooks.resource.bookorder.dto.response.BookOrderRegisterInvoiceResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderUserResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminModifyResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminResponse;
import store.mybooks.resource.bookorder.eumulation.BookOrderStatusName;
import store.mybooks.resource.bookorder.service.BookOrderService;
import store.mybooks.resource.bookorder.service.TotalOrderService;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;

/**
 * packageName    : store.mybooks.resource.book_order.controller<br>
 * fileName       : BookOrderRestControllerTest<br>
 * author         : minsu11<br>
 * date           : 3/3/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/3/24        minsu11       최초 생성<br>
 */
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(BookOrderRestController.class)
class BookOrderRestControllerTest {
    @MockBean
    private BookOrderService bookOrderService;
    @MockBean
    private TotalOrderService orderService;
    @Autowired
    private ObjectMapper mapper;
    private MockMvc mockMvc;
    private Long userId = 1L;
    private String url = "/api/orders";

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("주문 내역 조회 페이징 처리")
    void givenPageable_whenGetBookOrderResponseList_thenReturnBookOrderUserResponsePage() throws Exception {
        BookOrderUserResponse bookOrderUserResponse = new BookOrderUserResponse(
                BookOrderStatusName.ORDER_WAIT.toString(), "test", 100,
                LocalDate.of(1212, 12, 12), "test", "testName", "testAddress",
                "010-0000-0000", "testMessage", 1000, 100, 100,
                "123123123", 1L);

        OrderDetailInfoResponse orderDetailInfoResponse = new OrderDetailInfoResponse(
                1L, "test book", 1L, 1000, 2, false,
                "test image", "test order status", 1L);

        bookOrderUserResponse.createOrderDetailInfos(List.of(orderDetailInfoResponse));
        List<BookOrderUserResponse> bookOrderUserResponses =
                List.of(bookOrderUserResponse);
        Pageable pageable = PageRequest.of(0, 2);

        Page<BookOrderUserResponse> bookOrderPage =
                new PageImpl<>(bookOrderUserResponses, pageable, bookOrderUserResponses.size());

        when(bookOrderService.getBookOrderResponseList(any(), any())).thenReturn(bookOrderPage);
        mockMvc.perform(get(url + "/users?page=0&size=2")
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].statusId").value(BookOrderStatusName.ORDER_WAIT.toString()))
                .andExpect(jsonPath("$.content[0].deliveryRuleName").value("test"))
                .andExpect(jsonPath("$.content[0].deliveryCost").value(100))
                .andExpect(jsonPath("$.content[0].orderDate").value("1212-12-12"))
                .andExpect(jsonPath("$.content[0].invoiceNumber").value("test"))
                .andExpect(jsonPath("$.content[0].receiverName").value("testName"))
                .andExpect(jsonPath("$.content[0].receiverAddress").value("testAddress"))
                .andExpect(jsonPath("$.content[0].receiverPhoneNumber").value("010-0000-0000"))
                .andExpect(jsonPath("$.content[0].receiverMessage").value("testMessage"))
                .andExpect(jsonPath("$.content[0].totalCost").value(1000))
                .andExpect(jsonPath("$.content[0].pointCost").value(100))
                .andExpect(jsonPath("$.content[0].couponCost").value(100))
                .andExpect(jsonPath("$.content[0].number").value("123123123"))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].bookName").value("test book"))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].couponId").value(1L))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].cost").value(1000))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].amount").value(2))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].isCouponUsed").value(false))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].image").value("test image"))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].statusId").value("test order status"))
                .andExpect(jsonPath("$.content[0].orderDetailInfoList[0].orderDetailId").value(1L))
                .andDo(document("book-order-page",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content[].statusId").description("주문 상태"),
                                fieldWithPath("content[].deliveryRuleName").description("배송 규정 명"),
                                fieldWithPath("content[].deliveryCost").description("배송 비"),
                                fieldWithPath("content[].orderDate").description("주문일"),
                                fieldWithPath("content[].invoiceNumber").description("송장 번호"),
                                fieldWithPath("content[].receiverName").description("받는 사람 이름"),
                                fieldWithPath("content[].receiverAddress").description("받는 사람 주소"),
                                fieldWithPath("content[].receiverPhoneNumber").description("받는 사람 번호"),
                                fieldWithPath("content[].receiverMessage").description("배송 요청 사항"),
                                fieldWithPath("content[].totalCost").description("총합 금액"),
                                fieldWithPath("content[].pointCost").description("사용한 포인트 금액"),
                                fieldWithPath("content[].couponCost").description("할인 받은 쿠폰 금액"),
                                fieldWithPath("content[].number").description("주문 번호"),
                                fieldWithPath("content[].id").description("주문 아이디"),
                                fieldWithPath("content[].orderDetailInfoList[].id").description("상세 주문 아이디"),
                                fieldWithPath("content[].orderDetailInfoList[].bookName").description("주문한 도서 이름"),
                                fieldWithPath("content[].orderDetailInfoList[].couponId").description("사용한 유저 쿠폰 아이디"),
                                fieldWithPath("content[].orderDetailInfoList[].cost").description("도서 금액"),
                                fieldWithPath("content[].orderDetailInfoList[].amount").description("도서 수량"),
                                fieldWithPath("content[].orderDetailInfoList[].isCouponUsed").description("쿠폰 사용 유무"),
                                fieldWithPath("content[].orderDetailInfoList[].image").description("도서 이미지"),
                                fieldWithPath("content[].orderDetailInfoList[].statusId").description("주문 상세 상태"),
                                fieldWithPath("content[].orderDetailInfoList[].orderDetailId").description("상세 도서 아이디"),
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
                        )
                ));
    }

    @Test
    @DisplayName("관리자 주문 조회 페이징")
    void givenPageable_whenGetBookOrderAdminResponseList_thenReturnBookOrderAdminResponseList() throws Exception {
        BookOrderAdminResponse bookOrderAdminResponse = new BookOrderAdminResponse(1L, 1L, "test", LocalDate.of(1212, 12, 12),
                LocalDate.of(1212, 12, 12), "test invoice number", "test order number");

        List<BookOrderAdminResponse> bookOrderAdminResponses = List.of(bookOrderAdminResponse);

        Pageable pageable = PageRequest.of(0, 2);
        Page<BookOrderAdminResponse> bookOrderPage = new PageImpl<>(bookOrderAdminResponses,
                pageable, bookOrderAdminResponses.size());
        when(bookOrderService.getBookOrderAdminResponseList(any())).thenReturn(bookOrderPage);
        mockMvc.perform(get(url + "/admin?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].userId").value(1L))
                .andExpect(jsonPath("$.content[0].statusId").value("test"))
                .andExpect(jsonPath("$.content[0].date").value("1212-12-12"))
                .andExpect(jsonPath("$.content[0].outDate").value("1212-12-12"))
                .andExpect(jsonPath("$.content[0].invoiceNumber").value("test invoice number"))
                .andExpect(jsonPath("$.content[0].number").value("test order number"))
                .andDo(document("book-admin-order-page",
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content[].id").description("주문 아이디"),
                                fieldWithPath("content[].userId").description("회원 아이디"),
                                fieldWithPath("content[].statusId").description("주문 상태 명"),
                                fieldWithPath("content[].date").description("주문일"),
                                fieldWithPath("content[].outDate").description("출고일"),
                                fieldWithPath("content[].invoiceNumber").description("송장 번호"),
                                fieldWithPath("content[].number").description("주문 번호"),
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
                        )
                ));

        verify(bookOrderService, times(1)).getBookOrderAdminResponseList(any());
    }

    @Test
    @DisplayName("관리자 페이지에서 주문 상태 변경")
    void givenBookOrderAdminModifyRequest_whenModifyBookOrderAdminStatus_thenReturnBookOrderAdminModifyResponse() throws Exception {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();
        BookOrderAdminModifyResponse bookOrderAdminModifyResponse = new BookOrderAdminModifyResponse(1L, "test", LocalDate.of(1212, 12, 12));

        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "test12345");
        when(bookOrderService.modifyBookOrderAdminStatus(any())).thenReturn(bookOrderAdminModifyResponse);
        mockMvc.perform(
                        put("/api/orders/statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.statusId").value("test"))
                .andExpect(jsonPath("$.outDate").value("1212-12-12"))
                .andDo(document("book-order-admin-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("수정한 주문 아이디"),
                                fieldWithPath("statusId").description("수정한 주문 상태 "),
                                fieldWithPath("outDate").description("수정한 출고일")
                        ))
                );
        verify(bookOrderService, times(1)).modifyBookOrderAdminStatus(any());

    }

    @Test
    @DisplayName("관리자 페이지에서 주문 상태 변경 유효성 검사(id null일 때 실패)")
    void givenBookOrderAdminModifyRequest_whenHashErrors_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();

        ReflectionTestUtils.setField(request, "id", null);
        ReflectionTestUtils.setField(request, "invoiceNumber", "test12345");
        mockMvc.perform(
                        put("/api/orders/statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-admin-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).modifyBookOrderAdminStatus(any());

    }

    @Test
    @DisplayName("관리자 페이지에서 주문 상태 변경 유효성 검사(id 음수일 때 실패)")
    void givenBookOrderAdminModifyRequestIdNegative_whenHashErrors_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();

        ReflectionTestUtils.setField(request, "id", null);
        ReflectionTestUtils.setField(request, "invoiceNumber", "test12345");
        mockMvc.perform(
                        put("/api/orders/statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-admin-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).modifyBookOrderAdminStatus(any());

    }

    @Test
    @DisplayName("관리자 페이지에서 주문 상태 변경 유효성 검사 실패(송장 번호 8자리보다 작음)")
    void givenBookOrderAdminModifyRequestInvoiceNumberMinSize_whenHashError_thenThrowRequestValidationException() throws Exception {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();

        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "test123");
        mockMvc.perform(
                        put("/api/orders/statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-admin-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).modifyBookOrderAdminStatus(any());

    }

    @Test
    @DisplayName("관리자 페이지에서 주문 상태 변경 유효성 검사 실패(송장 번호 20자리 넘어감)")
    void givenBookOrderAdminModifyRequestInvoiceNumberMaxSize_whenHashError_thenThrowRequestValidationException() throws Exception {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();

        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "test123456789test12123123124dwasd3");
        mockMvc.perform(
                        put("/api/orders/statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-admin-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).modifyBookOrderAdminStatus(any());

    }

    @Test
    @DisplayName("관리자 페이지에서 주문 상태 변경 유효성 검사 실패(송장 번호 null 값")
    void givenBookOrderAdminModifyRequestInvoiceNumberNull_whenHashError_thenThrowRequestValidationException() throws Exception {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();

        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", null);
        mockMvc.perform(
                        put("/api/orders/statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-admin-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).modifyBookOrderAdminStatus(any());

    }

    @Test
    @DisplayName("관리자 페이지에서 주문 상태 변경 유효성 검사 실패(송장 번호 빈 값인 경우)")
    void givenBookOrderAdminModifyRequestInvoiceNumberEmpty_whenHashError_thenThrowRequestValidationException() throws Exception {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();

        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "");
        mockMvc.perform(
                        put("/api/orders/statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-admin-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).modifyBookOrderAdminStatus(any());

    }

    @Test
    @DisplayName("관리자 페이지에서 주문 상태 변경 유효성 검사 실패(송장 번호 8자리보다 작음)")
    void givenBookOrderAdminModifyRequestInvoiceNumberBlank_whenHashError_thenThrowRequestValidationException() throws Exception {
        BookOrderAdminModifyRequest request = new BookOrderAdminModifyRequest();

        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", " ");
        mockMvc.perform(
                        put("/api/orders/statuses")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-admin-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).modifyBookOrderAdminStatus(any());
    }


    @Test
    @DisplayName("송장 번호 등록 테스트 성공")
    void givenBookOrderRegisterInvoiceRequest_whenRegisterBookOrderInvoiceNumber_thenReturnBookOrderRegisterInvoiceResponse() throws Exception {
        BookOrderRegisterInvoiceRequest request = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "invoiceNumber");

        BookOrderRegisterInvoiceResponse response = new BookOrderRegisterInvoiceResponse("invoiceNumber");
        when(bookOrderService.registerBookOrderInvoiceNumber(any())).thenReturn(response);
        mockMvc.perform(put(url + "/invoiceNumbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.invoiceNumber").value("invoiceNumber"))
                .andDo(document("book-order-register-invoice",
                        requestFields(
                                fieldWithPath("id").description("송장 번호 등록할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("등록할 송장 번호")
                        ),
                        responseFields(
                                fieldWithPath("invoiceNumber").description("등록한 송장 번호")
                        )
                ));
        verify(bookOrderService, times(1)).registerBookOrderInvoiceNumber(any());

    }

    @Test
    @DisplayName("송장 번호 등록 유효성 실패 검사(id null 유효성 검사)")
    void givenBookOrderRegisterInvoiceRequest_whenValidateRequest_thenThrowRequestValidationException() throws Exception {
        BookOrderRegisterInvoiceRequest request = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(request, "id", null);
        ReflectionTestUtils.setField(request, "invoiceNumber", "invoiceNumber");
        mockMvc.perform(put(url + "/invoiceNumbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-register-invoice",
                        requestFields(
                                fieldWithPath("id").description("송장 번호 등록할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).registerBookOrderInvoiceNumber(any());

    }

    @Test
    @DisplayName("송장 번호 등록 유효성 실패 검사(id 음수 유효성 검사)")
    void givenBookOrderRegisterInvoiceRequestIdNegativeNum_whenValidateRequest_thenThrowRequestValidationException() throws Exception {
        BookOrderRegisterInvoiceRequest request = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(request, "id", -1L);
        ReflectionTestUtils.setField(request, "invoiceNumber", "invoiceNumber");
        mockMvc.perform(put(url + "/invoiceNumbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-register-invoice",
                        requestFields(
                                fieldWithPath("id").description("송장 번호 등록할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).registerBookOrderInvoiceNumber(any());

    }

    @Test
    @DisplayName("송장 번호 등록 유효성 실패 검사(송장 번호 null인 경우)")
    void givenBookOrderRegisterInvoiceRequestInvoiceNumberNull_whenValidateRequest_thenThrowRequestValidationException() throws Exception {
        BookOrderRegisterInvoiceRequest invoiceRequest = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(invoiceRequest, "id", 1L);
        ReflectionTestUtils.setField(invoiceRequest, "invoiceNumber", null);
        mockMvc.perform(put(url + "/invoiceNumbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invoiceRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-register-invoice",
                        requestFields(
                                fieldWithPath("id").description("송장 번호 등록할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).registerBookOrderInvoiceNumber(any());

    }

    @Test
    @DisplayName("송장 번호 등록 유효성 실패 검사(송장 번호 빈 값인 경우)")
    void givenBookOrderRegisterInvoiceRequestInvoiceNumberEmpty_whenValidateRequest_thenThrowRequestValidationException() throws Exception {
        BookOrderRegisterInvoiceRequest invoiceRequest = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(invoiceRequest, "id", 1L);
        ReflectionTestUtils.setField(invoiceRequest, "invoiceNumber", "");
        mockMvc.perform(put(url + "/invoiceNumbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invoiceRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-register-invoice",
                        requestFields(
                                fieldWithPath("id").description("송장 번호 등록할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).registerBookOrderInvoiceNumber(any());

    }

    @Test
    @DisplayName("송장 번호 등록 유효성 실패 검사(송장 번호 공백인 경우)")
    void givenBookOrderRegisterInvoiceRequestInvoiceNumberBlank_whenValidateRequest_thenThrowRequestValidationException() throws Exception {
        BookOrderRegisterInvoiceRequest invoiceRequestEmptyRequest = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(invoiceRequestEmptyRequest, "id", 1L);
        ReflectionTestUtils.setField(invoiceRequestEmptyRequest, "invoiceNumber", " ");
        mockMvc.perform(put(url + "/invoiceNumbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invoiceRequestEmptyRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-register-invoice",
                        requestFields(
                                fieldWithPath("id").description("송장 번호 등록할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).registerBookOrderInvoiceNumber(any());

    }

    @Test
    @DisplayName("송장 번호 등록 유효성 실패 검사(송장 번호 8자리 미만인 경우)")
    void givenBookOrderRegisterInvoiceRequestInvoiceNumberMinSize_whenValidateRequest_thenThrowRequestValidationException() throws Exception {
        BookOrderRegisterInvoiceRequest invoiceMinRequest = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(invoiceMinRequest, "id", 1L);
        ReflectionTestUtils.setField(invoiceMinRequest, "invoiceNumber", "test123");
        mockMvc.perform(put(url + "/invoiceNumbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invoiceMinRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-register-invoice",
                        requestFields(
                                fieldWithPath("id").description("송장 번호 등록할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).registerBookOrderInvoiceNumber(any());

    }

    @Test
    @DisplayName("송장 번호 등록 유효성 실패 검사(송장 번호 20자리 넘는 경우)")
    void givenBookOrderRegisterInvoiceRequestInvoiceNumberMaxOverSize_whenValidateRequest_thenThrowRequestValidationException() throws Exception {
        BookOrderRegisterInvoiceRequest invoiceMaxRequest = new BookOrderRegisterInvoiceRequest();
        ReflectionTestUtils.setField(invoiceMaxRequest, "id", 1L);
        ReflectionTestUtils.setField(invoiceMaxRequest, "invoiceNumber", "test123456test1234567");
        mockMvc.perform(put(url + "/invoiceNumbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invoiceMaxRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-register-invoice",
                        requestFields(
                                fieldWithPath("id").description("송장 번호 등록할 주문 아이디"),
                                fieldWithPath("invoiceNumber").description("송장 번호")
                        )
                ));
        verify(bookOrderService, never()).registerBookOrderInvoiceNumber(any());
    }

    @Test
    @DisplayName("주소가 있는지 성공 테스트")
    void givenId_whenCheckUserOrderAddress_thenReturnBoolean() throws Exception {
        when(bookOrderService.checkUserOrderAddress(anyLong())).thenReturn(true);
        mockMvc.perform(get(url + "/check/address/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"))
                .andDo(document("book-order-address-check",
                        pathParameters(
                                parameterWithName("id").description("주소 확인할 주소 아이디")
                        )

                ));
        verify(bookOrderService, times(1)).checkUserOrderAddress(anyLong());
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void givenBookOrderCreateRequest_whenCreateOrder_thenReturnBookOrderCreateResponse() throws Exception {
        BookOrderCreateRequest request = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(request, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(request, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(request, "orderNumber", "test");
        ReflectionTestUtils.setField(request, "pointCost", 0);
        ReflectionTestUtils.setField(request, "couponCost", 0);
        ReflectionTestUtils.setField(request, "totalCost", 1000);
        ReflectionTestUtils.setField(request, "wrapCost", 500);
        ReflectionTestUtils.setField(request, "orderCode", null);

        BookOrderCreateResponse response =
                new BookOrderCreateResponse("주문 대기", "test", 1000, false);
        when(orderService.createOrder(any(), anyLong())).thenReturn(response);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderStatus").value("주문 대기"))
                .andExpect(jsonPath("$.number").value("test"))
                .andExpect(jsonPath("$.totalCost").value(1000))
                .andExpect(jsonPath("$.isCouponUsed").value(false))
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        ),
                        responseFields(
                                fieldWithPath("orderStatus").description("주문 생성시 주문 대기 상태"),
                                fieldWithPath("number").description("주문 번호"),
                                fieldWithPath("totalCost").description("주문 할 금액"),
                                fieldWithPath("isCouponUsed").description("쿠폰을 적용했는지 여부")
                        )
                ));
        verify(orderService, times(1)).createOrder(any(), anyLong());
    }


    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(이름이 null 인 경우")
    void givenBookOrderCreateRequestNameNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest nameNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(nameNullRequest, "name", null);
        ReflectionTestUtils.setField(nameNullRequest, "email", "test@test.com");
        ReflectionTestUtils.setField(nameNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(nameNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(nameNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(nameNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(nameNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(nameNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(nameNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(nameNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(nameNullRequest, "orderCode", null);


        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(nameNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(이름이 빈 값 인 경우")
    void givenBookOrderCreateRequestNameEmpty_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest nameEmptyRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(nameEmptyRequest, "name", "");
        ReflectionTestUtils.setField(nameEmptyRequest, "email", "test@test.com");
        ReflectionTestUtils.setField(nameEmptyRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(nameEmptyRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(nameEmptyRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(nameEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(nameEmptyRequest, "pointCost", 0);
        ReflectionTestUtils.setField(nameEmptyRequest, "couponCost", 0);
        ReflectionTestUtils.setField(nameEmptyRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(nameEmptyRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(nameEmptyRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(nameEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(이름이 공백 인 경우")
    void givenBookOrderCreateRequestNameBlank_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest nameBlankRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(nameBlankRequest, "name", " ");
        ReflectionTestUtils.setField(nameBlankRequest, "email", "test@test.com");
        ReflectionTestUtils.setField(nameBlankRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(nameBlankRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(nameBlankRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(nameBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(nameBlankRequest, "pointCost", 0);
        ReflectionTestUtils.setField(nameBlankRequest, "couponCost", 0);
        ReflectionTestUtils.setField(nameBlankRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(nameBlankRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(nameBlankRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(nameBlankRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(이메일이 null 인 경우")
    void givenBookOrderCreateRequestEmailNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailNullRequest, "name", "test");
        ReflectionTestUtils.setField(emailNullRequest, "email", null);
        ReflectionTestUtils.setField(emailNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailNullRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(emailNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(이메일 빈 값 인 경우")
    void givenBookOrderCreateRequestEmailEmpty_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailEmptyRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailEmptyRequest, "name", "test");
        ReflectionTestUtils.setField(emailEmptyRequest, "email", "");
        ReflectionTestUtils.setField(emailEmptyRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailEmptyRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailEmptyRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailEmptyRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailEmptyRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailEmptyRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailEmptyRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailEmptyRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(emailEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(이메일 공백 인 경우")
    void givenBookOrderCreateRequestEmailBlank_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailBlankRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailBlankRequest, "name", "test");
        ReflectionTestUtils.setField(emailBlankRequest, "email", " ");
        ReflectionTestUtils.setField(emailBlankRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailBlankRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailBlankRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailBlankRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailBlankRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailBlankRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailBlankRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailBlankRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(emailBlankRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(이메일이 email 형식이 아닌  경우")
    void givenBookOrderCreateRequestNotEmailFormat_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailNotFormatRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailNotFormatRequest, "name", "test");
        ReflectionTestUtils.setField(emailNotFormatRequest, "email", "test");
        ReflectionTestUtils.setField(emailNotFormatRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailNotFormatRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailNotFormatRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailNotFormatRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailNotFormatRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailNotFormatRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailNotFormatRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailNotFormatRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailNotFormatRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(emailNotFormatRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }


    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(이메일 길이가 30이상 인 경우")
    void givenBookOrderCreateRequestEmail30OverSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailMaxOverRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailMaxOverRequest, "name", "test");
        ReflectionTestUtils.setField(emailMaxOverRequest, "email", "test1234test1234test1234test1234test1234@naver.com");
        ReflectionTestUtils.setField(emailMaxOverRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailMaxOverRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailMaxOverRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailMaxOverRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailMaxOverRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailMaxOverRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailMaxOverRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailMaxOverRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailMaxOverRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(emailMaxOverRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(회원의 번호가 null인 경우")
    void givenBookOrderCreateRequestPhoneNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneNullRequest, "name", "test");
        ReflectionTestUtils.setField(phoneNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneNullRequest, "phone", null);
        ReflectionTestUtils.setField(phoneNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneNullRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(phoneNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(회원의 번호가 빈 값인 경우")
    void givenBookOrderCreateRequestPhoneEmpty_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneEmptyRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneEmptyRequest, "name", "test");
        ReflectionTestUtils.setField(phoneEmptyRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneEmptyRequest, "phone", "");
        ReflectionTestUtils.setField(phoneEmptyRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneEmptyRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneEmptyRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneEmptyRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneEmptyRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneEmptyRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneEmptyRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(phoneEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(회원의 번호가 공백인 경우")
    void givenBookOrderCreateRequestPhoneBlank_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneBlankRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneBlankRequest, "name", "test");
        ReflectionTestUtils.setField(phoneBlankRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneBlankRequest, "phone", " ");
        ReflectionTestUtils.setField(phoneBlankRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneBlankRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneBlankRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneBlankRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneBlankRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneBlankRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneBlankRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(phoneBlankRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(회원의 번호 길이가 10 미만 경우")
    void givenBookOrderCreateRequestPhoneMinSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneMinUnderRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneMinUnderRequest, "name", "test");
        ReflectionTestUtils.setField(phoneMinUnderRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneMinUnderRequest, "phone", "010123");
        ReflectionTestUtils.setField(phoneMinUnderRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneMinUnderRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneMinUnderRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneMinUnderRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneMinUnderRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneMinUnderRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneMinUnderRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneMinUnderRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(phoneMinUnderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(회원의 번호 길이가 13 초과한 경우")
    void givenBookOrderCreateRequestPhoneMaxSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneMaxOverRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneMaxOverRequest, "name", "test");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "phone", "010-1234-12345");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(phoneMaxOverRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(주문한 도서 정보 목록이 null경우")
    void givenBookOrderCreateRequestBookOrderInfoNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneMaxOverRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneMaxOverRequest, "name", "test");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "bookInfoList", null);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(phoneMaxOverRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(주문한 정보가 null경우")
    void givenBookOrderCreateRequestOrderInfoNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest orderInfoNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(orderInfoNullRequest, "name", "test");
        ReflectionTestUtils.setField(orderInfoNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(orderInfoNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(orderInfoNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(orderInfoNullRequest, "orderInfo", null);
        ReflectionTestUtils.setField(orderInfoNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(orderInfoNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(orderInfoNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(orderInfoNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(orderInfoNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(orderInfoNullRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(orderInfoNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo").description("주문한 도서 목록"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(주문 번호가 null경우")
    void givenBookOrderCreateRequestBookNumberNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest orderNumberNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(orderNumberNullRequest, "name", "test");
        ReflectionTestUtils.setField(orderNumberNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(orderNumberNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(orderNumberNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(orderNumberNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(orderNumberNullRequest, "orderNumber", null);
        ReflectionTestUtils.setField(orderNumberNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(orderNumberNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(orderNumberNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(orderNumberNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(orderNumberNullRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(orderNumberNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(주문 번호가 빈 값인 경우")
    void givenBookOrderCreateRequestBookOrderNumberEmpty_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest bookOrderEmptyRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "name", "test");
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "orderNumber", "");
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "pointCost", 0);
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "couponCost", 0);
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(bookOrderEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(주문 번호가 공백인 경우")
    void givenBookOrderCreateRequestBookOrderNumberBlank_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest bookOrderBlankRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(bookOrderBlankRequest, "name", "test");
        ReflectionTestUtils.setField(bookOrderBlankRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(bookOrderBlankRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(bookOrderBlankRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(bookOrderBlankRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(bookOrderBlankRequest, "orderNumber", " ");
        ReflectionTestUtils.setField(bookOrderBlankRequest, "pointCost", 0);
        ReflectionTestUtils.setField(bookOrderBlankRequest, "couponCost", 0);
        ReflectionTestUtils.setField(bookOrderBlankRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(bookOrderBlankRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(bookOrderBlankRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(bookOrderBlankRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(주문 번호의 사이즈가 20보다 넘는 경우")
    void givenBookOrderCreateRequestBookOrderNumberMaxOverSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest bookOrderMaxOverSizeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "name", "test");
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "pointCost", 0);
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "couponCost", 0);
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(bookOrderMaxOverSizeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(포인트가 null인 경우")
    void givenBookOrderCreateRequestPointCostNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest pointNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(pointNullRequest, "name", "test");
        ReflectionTestUtils.setField(pointNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(pointNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(pointNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(pointNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(pointNullRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(pointNullRequest, "pointCost", null);
        ReflectionTestUtils.setField(pointNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(pointNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(pointNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(pointNullRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(pointNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(포인트가 음수인 경우")
    void givenBookOrderCreateRequestPointCostNegative_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest pointNegativeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(pointNegativeRequest, "name", "test");
        ReflectionTestUtils.setField(pointNegativeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(pointNegativeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(pointNegativeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(pointNegativeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(pointNegativeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(pointNegativeRequest, "pointCost", -1);
        ReflectionTestUtils.setField(pointNegativeRequest, "couponCost", 0);
        ReflectionTestUtils.setField(pointNegativeRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(pointNegativeRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(pointNegativeRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(pointNegativeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(쿠폰가 null인 경우")
    void givenBookOrderCreateRequestCouponCostNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest couponCostNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(couponCostNullRequest, "name", "test");
        ReflectionTestUtils.setField(couponCostNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(couponCostNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(couponCostNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(couponCostNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(couponCostNullRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(couponCostNullRequest, "pointCost", 1);
        ReflectionTestUtils.setField(couponCostNullRequest, "couponCost", null);
        ReflectionTestUtils.setField(couponCostNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(couponCostNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(couponCostNullRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(couponCostNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(쿠폰 값이 음수인 경우")
    void givenBookOrderCreateRequestCouponCostNegative_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest couponCostNegativeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(couponCostNegativeRequest, "name", "test");
        ReflectionTestUtils.setField(couponCostNegativeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(couponCostNegativeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(couponCostNegativeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(couponCostNegativeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(couponCostNegativeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(couponCostNegativeRequest, "pointCost", 1);
        ReflectionTestUtils.setField(couponCostNegativeRequest, "couponCost", -1);
        ReflectionTestUtils.setField(couponCostNegativeRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(couponCostNegativeRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(couponCostNegativeRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(couponCostNegativeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(총액이 null인 경우")
    void givenBookOrderCreateRequestTotalCostNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest totalCostNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(totalCostNullRequest, "name", "test");
        ReflectionTestUtils.setField(totalCostNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(totalCostNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(totalCostNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(totalCostNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(totalCostNullRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(totalCostNullRequest, "pointCost", 1);
        ReflectionTestUtils.setField(totalCostNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(totalCostNullRequest, "totalCost", null);
        ReflectionTestUtils.setField(totalCostNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(totalCostNullRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(totalCostNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(총액이 음수인 경우")
    void givenBookOrderCreateRequestTotalCostNegative_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest totalCostNegativeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(totalCostNegativeRequest, "name", "test");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "pointCost", 1);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "couponCost", 0);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "totalCost", -1);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(totalCostNegativeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(총액이 음수인 경우")
    void givenBookOrderCreateRequestWrapCostNegative_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest wrapCostNegativeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "name", "test");
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "pointCost", 1);
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "couponCost", 0);
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "totalCost", 100);
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "wrapCost", -1);
        ReflectionTestUtils.setField(wrapCostNegativeRequest, "orderCode", null);

        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(wrapCostNegativeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 번호가 있는지 확인")
    void givenOrderNumber_whenCheckBookOrderNumberExists_thenReturnBoolean() throws Exception {
        when(bookOrderService.checkBookOrderNumberExists(anyString())).thenReturn(true);

        mockMvc.perform(get(url + "/orderNumber/{orderNumber}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"))
                .andDo(document("book-order-number-check",
                        pathParameters(
                                parameterWithName("orderNumber").description("조회할 주문 번호")
                        )));
        verify(bookOrderService, times(1)).checkBookOrderNumberExists(anyString());
    }

    @Test
    @DisplayName("비회원 주문 시 주문지 생성")
    void givenBookOrderCreateRequestNonUser_whenCreateOrder_thenReturnBookOrderCreateResponse() throws Exception {
        BookOrderCreateRequest request = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(request, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(request, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(request, "orderNumber", "test");
        ReflectionTestUtils.setField(request, "pointCost", 0);
        ReflectionTestUtils.setField(request, "couponCost", 0);
        ReflectionTestUtils.setField(request, "totalCost", 1000);
        ReflectionTestUtils.setField(request, "wrapCost", 500);
        ReflectionTestUtils.setField(request, "orderCode", "12345");

        BookOrderCreateResponse response =
                new BookOrderCreateResponse("주문 대기", "test",
                        1000, false);
        when(orderService.createOrder(any(), anyLong())).thenReturn(response);
        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderStatus").value("주문 대기"))
                .andExpect(jsonPath("$.number").value("test"))
                .andExpect(jsonPath("$.totalCost").value(1000))
                .andExpect(jsonPath("$.isCouponUsed").value(false))

                .andDo(document("book-order-non-user",
                        requestFields(
                                fieldWithPath("name").description("비회원 이름"),
                                fieldWithPath("email").description("비회원 이메일"),
                                fieldWithPath("phone").description("비회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        ),
                        responseFields(
                                fieldWithPath("orderStatus").description("주문 상태"),
                                fieldWithPath("number").description("주문 번호"),
                                fieldWithPath("totalCost").description("주문 총액"),
                                fieldWithPath("isCouponUsed").description("쿠폰 사용 유무")
                        )));
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(이름이 null 인 경우")
    void givenBookNonUserOrderCreateRequestNameNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest nameNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(nameNullRequest, "name", null);
        ReflectionTestUtils.setField(nameNullRequest, "email", "test@test.com");
        ReflectionTestUtils.setField(nameNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(nameNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(nameNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(nameNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(nameNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(nameNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(nameNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(nameNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(nameNullRequest, "orderCode", "12345");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(nameNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 비회원 주문 생성 시 유효성 실패 검사(이름이 빈 값 인 경우")
    void givenBookOrderNonUserCreateRequestNameEmpty_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest nameEmptyRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(nameEmptyRequest, "name", "");
        ReflectionTestUtils.setField(nameEmptyRequest, "email", "test@test.com");
        ReflectionTestUtils.setField(nameEmptyRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(nameEmptyRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(nameEmptyRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(nameEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(nameEmptyRequest, "pointCost", 0);
        ReflectionTestUtils.setField(nameEmptyRequest, "couponCost", 0);
        ReflectionTestUtils.setField(nameEmptyRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(nameEmptyRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(nameEmptyRequest, "orderCode", "12345");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(nameEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(이름이 공백 인 경우")
    void givenBookOrderNonUserCreateRequestNameBlank_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest nameBlankRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(nameBlankRequest, "name", " ");
        ReflectionTestUtils.setField(nameBlankRequest, "email", "test@test.com");
        ReflectionTestUtils.setField(nameBlankRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(nameBlankRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(nameBlankRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(nameBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(nameBlankRequest, "pointCost", 0);
        ReflectionTestUtils.setField(nameBlankRequest, "couponCost", 0);
        ReflectionTestUtils.setField(nameBlankRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(nameBlankRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(nameBlankRequest, "orderCode", "12345");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(nameBlankRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(이메일이 null 인 경우")
    void givenBookOrderNonUserCreateRequestEmailNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailNullRequest, "name", "test");
        ReflectionTestUtils.setField(emailNullRequest, "email", null);
        ReflectionTestUtils.setField(emailNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailNullRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(emailNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 금액")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(이메일 빈 값 인 경우")
    void givenBookOrderNonUserCreateRequestEmailEmpty_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailEmptyRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailEmptyRequest, "name", "test");
        ReflectionTestUtils.setField(emailEmptyRequest, "email", "");
        ReflectionTestUtils.setField(emailEmptyRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailEmptyRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailEmptyRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailEmptyRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailEmptyRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailEmptyRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailEmptyRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailEmptyRequest, "orderCode", "12345");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(emailEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(이메일 공백 인 경우")
    void givenBookOrderNonUserCreateRequestEmailBlank_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailBlankRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailBlankRequest, "name", "test");
        ReflectionTestUtils.setField(emailBlankRequest, "email", " ");
        ReflectionTestUtils.setField(emailBlankRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailBlankRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailBlankRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailBlankRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailBlankRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailBlankRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailBlankRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailBlankRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(emailBlankRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(이메일이 email 형식이 아닌  경우")
    void givenBookOrderNonUserCreateRequestNotEmailFormat_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailNotFormatRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailNotFormatRequest, "name", "test");
        ReflectionTestUtils.setField(emailNotFormatRequest, "email", "test");
        ReflectionTestUtils.setField(emailNotFormatRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailNotFormatRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailNotFormatRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailNotFormatRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailNotFormatRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailNotFormatRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailNotFormatRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailNotFormatRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailNotFormatRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(emailNotFormatRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }


    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(이메일 길이가 30이상 인 경우")
    void givenBookOrderNonUserCreateRequestEmail30OverSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest emailMaxOverRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(emailMaxOverRequest, "name", "test");
        ReflectionTestUtils.setField(emailMaxOverRequest, "email", "test1234test1234test1234test1234test1234@naver.com");
        ReflectionTestUtils.setField(emailMaxOverRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(emailMaxOverRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(emailMaxOverRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(emailMaxOverRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(emailMaxOverRequest, "pointCost", 0);
        ReflectionTestUtils.setField(emailMaxOverRequest, "couponCost", 0);
        ReflectionTestUtils.setField(emailMaxOverRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(emailMaxOverRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(emailMaxOverRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(emailMaxOverRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 콛,")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(회원의 번호가 null인 경우")
    void givenBookOrderNonUserCreateRequestPhoneNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneNullRequest, "name", "test");
        ReflectionTestUtils.setField(phoneNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneNullRequest, "phone", null);
        ReflectionTestUtils.setField(phoneNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneNullRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(phoneNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 콛,")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(회원의 번호가 빈 값인 경우")
    void givenBookOrderNonUserCreateRequestPhoneEmpty_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneEmptyRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneEmptyRequest, "name", "test");
        ReflectionTestUtils.setField(phoneEmptyRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneEmptyRequest, "phone", "");
        ReflectionTestUtils.setField(phoneEmptyRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneEmptyRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneEmptyRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneEmptyRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneEmptyRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneEmptyRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneEmptyRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(phoneEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(회원의 번호가 공백인 경우")
    void givenBookOrderNonUserCreateRequestPhoneBlank_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneBlankRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneBlankRequest, "name", "test");
        ReflectionTestUtils.setField(phoneBlankRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneBlankRequest, "phone", " ");
        ReflectionTestUtils.setField(phoneBlankRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneBlankRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneBlankRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneBlankRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneBlankRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneBlankRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneBlankRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(phoneBlankRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(회원의 번호 길이가 10 미만 경우")
    void givenBookOrderNonUserCreateRequestPhoneMinSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneMinUnderRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneMinUnderRequest, "name", "test");
        ReflectionTestUtils.setField(phoneMinUnderRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneMinUnderRequest, "phone", "010123");
        ReflectionTestUtils.setField(phoneMinUnderRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneMinUnderRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneMinUnderRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneMinUnderRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneMinUnderRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneMinUnderRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneMinUnderRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneMinUnderRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(phoneMinUnderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(회원의 번호 길이가 13 초과한 경우")
    void givenBookOrderNonUSerCreateRequestPhoneMaxSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneMaxOverRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneMaxOverRequest, "name", "test");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "phone", "010-1234-12345");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(phoneMaxOverRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(주문한 도서 정보 목록이 null경우")
    void givenBookOrderCreateRequestNonUserBookOrderInfoNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest phoneMaxOverRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(phoneMaxOverRequest, "name", "test");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "bookInfoList", null);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(phoneMaxOverRequest, "pointCost", 0);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "couponCost", 0);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(phoneMaxOverRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(phoneMaxOverRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-validation",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")

                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(주문한 정보가 null경우")
    void givenBookOrderNonUserCreateRequestOrderInfoNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest orderInfoNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(orderInfoNullRequest, "name", "test");
        ReflectionTestUtils.setField(orderInfoNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(orderInfoNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(orderInfoNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(orderInfoNullRequest, "orderInfo", null);
        ReflectionTestUtils.setField(orderInfoNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(orderInfoNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(orderInfoNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(orderInfoNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(orderInfoNullRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(orderInfoNullRequest, "orderCode", "1234");

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(orderInfoNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo").description("주문한 도서 목록"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(주문 번호가 null경우")
    void givenBookOrderNonUserCreateRequestBookNumberNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest orderNumberNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(orderNumberNullRequest, "name", "test");
        ReflectionTestUtils.setField(orderNumberNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(orderNumberNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(orderNumberNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(orderNumberNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(orderNumberNullRequest, "orderNumber", null);
        ReflectionTestUtils.setField(orderNumberNullRequest, "pointCost", 0);
        ReflectionTestUtils.setField(orderNumberNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(orderNumberNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(orderNumberNullRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(orderNumberNullRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(orderNumberNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-Validation",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 생성 시 유효성 실패 검사(주문 번호가 빈 값인 경우")
    void givenBookOrderNonUserCreateRequestBookOrderNumberEmpty_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest bookOrderEmptyRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "name", "test");
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "orderNumber", "");
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "pointCost", 0);
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "couponCost", 0);
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "wrapCost", 500);
        ReflectionTestUtils.setField(bookOrderEmptyRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(bookOrderEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(주문 번호가 공백인 경우")
    void givenBookOrderNonUserCreateRequestBookOrderNumberBlank_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest bookOrderBlankRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(bookOrderBlankRequest, "name", "test");
        ReflectionTestUtils.setField(bookOrderBlankRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(bookOrderBlankRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(bookOrderBlankRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(bookOrderBlankRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(bookOrderBlankRequest, "orderNumber", " ");
        ReflectionTestUtils.setField(bookOrderBlankRequest, "pointCost", 0);
        ReflectionTestUtils.setField(bookOrderBlankRequest, "couponCost", 0);
        ReflectionTestUtils.setField(bookOrderBlankRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(bookOrderBlankRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(bookOrderBlankRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(bookOrderBlankRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-validation-empty",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(주문 번호의 사이즈가 20보다 넘는 경우")
    void givenBookOrderNonUserCreateRequestBookOrderNumberMaxOverSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest bookOrderMaxOverSizeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "name", "test");
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "pointCost", 0);
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "couponCost", 0);
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(bookOrderMaxOverSizeRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(bookOrderMaxOverSizeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-validation",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(포인트가 null인 경우")
    void givenBookOrderNonUserCreateRequestPointCostNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest pointNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(pointNullRequest, "name", "test");
        ReflectionTestUtils.setField(pointNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(pointNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(pointNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(pointNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(pointNullRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(pointNullRequest, "pointCost", null);
        ReflectionTestUtils.setField(pointNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(pointNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(pointNullRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(pointNullRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(pointNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-validation-null",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(포인트가 음수인 경우")
    void givenBookOrderNonUserCreateRequestPointCostNegative_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest pointNegativeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(pointNegativeRequest, "name", "test");
        ReflectionTestUtils.setField(pointNegativeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(pointNegativeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(pointNegativeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(pointNegativeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(pointNegativeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(pointNegativeRequest, "pointCost", -1);
        ReflectionTestUtils.setField(pointNegativeRequest, "couponCost", 0);
        ReflectionTestUtils.setField(pointNegativeRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(pointNegativeRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(pointNegativeRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(pointNegativeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-non-user-info-validation-negative",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(쿠폰가 null인 경우")
    void givenBookOrderNonUserCreateRequestCouponCostNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest couponCostNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(couponCostNullRequest, "name", "test");
        ReflectionTestUtils.setField(couponCostNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(couponCostNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(couponCostNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(couponCostNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(couponCostNullRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(couponCostNullRequest, "pointCost", 1);
        ReflectionTestUtils.setField(couponCostNullRequest, "couponCost", null);
        ReflectionTestUtils.setField(couponCostNullRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(couponCostNullRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(couponCostNullRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(couponCostNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비히원 주문 생성 시 유효성 실패 검사(쿠폰 값이 음수인 경우")
    void givenBookOrderNonUserCreateRequestCouponCostNegative_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest couponCostNegativeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(couponCostNegativeRequest, "name", "test");
        ReflectionTestUtils.setField(couponCostNegativeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(couponCostNegativeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(couponCostNegativeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(couponCostNegativeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(couponCostNegativeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(couponCostNegativeRequest, "pointCost", 1);
        ReflectionTestUtils.setField(couponCostNegativeRequest, "couponCost", -1);
        ReflectionTestUtils.setField(couponCostNegativeRequest, "totalCost", 1000);
        ReflectionTestUtils.setField(couponCostNegativeRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(couponCostNegativeRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .header("X-User-Id", userId)
                        .content(mapper.writeValueAsString(couponCostNegativeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(총액이 null인 경우")
    void givenBookOrderNonUserCreateRequestTotalCostNull_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest totalCostNullRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(totalCostNullRequest, "name", "test");
        ReflectionTestUtils.setField(totalCostNullRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(totalCostNullRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(totalCostNullRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(totalCostNullRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(totalCostNullRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(totalCostNullRequest, "pointCost", 1);
        ReflectionTestUtils.setField(totalCostNullRequest, "couponCost", 0);
        ReflectionTestUtils.setField(totalCostNullRequest, "totalCost", null);
        ReflectionTestUtils.setField(totalCostNullRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(totalCostNullRequest, "orderCode", null);
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(totalCostNullRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(총액이 음수인 경우")
    void givenBookOrderNonUserCreateRequestTotalCostNegative_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest totalCostNegativeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(totalCostNegativeRequest, "name", "test");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "pointCost", 1);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "couponCost", 0);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "totalCost", -1);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderCode", "1234");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(totalCostNegativeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 주문 생성 시 유효성 실패 검사(주문 코드가 6자리 넘는 경우")
    void givenBookOrderNonUserCreateRequestOrderCodeMaxOverSize_whenCreateOrder_thenThrowRequestValidationFailedException() throws Exception {
        BookOrderCreateRequest totalCostNegativeRequest = new BookOrderCreateRequest();
        ReflectionTestUtils.setField(totalCostNegativeRequest, "name", "test");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "email", "test1234@naver.com");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "phone", "010-1234-1234");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "bookInfoList", new ArrayList<BookInfoRequest>());
        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderInfo", new BookOrderInfoRequest());
        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderNumber", "test123456tset1234566778tes");
        ReflectionTestUtils.setField(totalCostNegativeRequest, "pointCost", 1);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "couponCost", 0);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "totalCost", -1);
        ReflectionTestUtils.setField(totalCostNegativeRequest, "wrapCost", 500);

        ReflectionTestUtils.setField(totalCostNegativeRequest, "orderCode", "1234567");
        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(totalCostNegativeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("book-order-info-create",

                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("회원 전화 번호"),
                                fieldWithPath("bookInfoList").description("주문한 도서 목록"),
                                fieldWithPath("orderInfo.deliveryId").description("주문 정보에 배송 규정 아이디"),
                                fieldWithPath("orderInfo.deliveryDate").description("사용자가 원하는 배송 날짜"),
                                fieldWithPath("orderInfo.recipientName").description("받는 사람 이름"),
                                fieldWithPath("orderInfo.recipientAddress").description("받는 사람의 주소"),
                                fieldWithPath("orderInfo.recipientPhoneNumber").description("받는 사람 전화 번호"),
                                fieldWithPath("orderInfo.receiverMessage").description("배송 시 요청사항"),
                                fieldWithPath("orderInfo.usingPoint").description("주문에 사용한 포인트"),
                                fieldWithPath("orderInfo.wrapCost").description("포장지 가격"),
                                fieldWithPath("orderInfo.couponApplicationAmount").description("쿠폰 적용 금약"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("pointCost").description("포인트 적립금"),
                                fieldWithPath("couponCost").description("총 쿠폰 적용 금액"),
                                fieldWithPath("totalCost").description("결제 할 금액"),
                                fieldWithPath("wrapCost").description("포장지 금액"),
                                fieldWithPath("orderCode").description("주문 코드")
                        )
                ));
        verify(orderService, never()).createOrder(any(), anyLong());
    }

    @Test
    @DisplayName("주문 번호로 결제 정보 조회")
    void givenOrderNumber_whenGetBookInfo_thenReturnBookOrderInfoPayResponse() throws Exception {
        BookOrderInfoPayResponse response = new BookOrderInfoPayResponse(
                "주문 대기", "test", 1000, true, 1000,
                new ArrayList<OrderDetailInfoResponse>());

        when(bookOrderService.getBookInfo(anyString())).thenReturn(response);

        mockMvc.perform(get(url + "/info/pay/{orderNumber}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderStatus").value("주문 대기"))
                .andExpect(jsonPath("$.number").value("test"))
                .andExpect(jsonPath("$.totalCost").value(1000))
                .andExpect(jsonPath("$.isCouponUsed").value(true))
                .andDo(document("book-order-get-pay-info",
                        pathParameters(
                                parameterWithName("orderNumber").description("주문 번호")
                        ),
                        responseFields(
                                fieldWithPath("orderStatus").description("주문 상태"),
                                fieldWithPath("number").description("주문 번호"),
                                fieldWithPath("totalCost").description("주문 총 액"),
                                fieldWithPath("isCouponUsed").description("쿠폰 적용 여부"),
                                fieldWithPath("pointCost").description("포인트"),
                                fieldWithPath("orderDetails").description("주문 상세 정보 목록")
                        )));


    }

    @Test
    @DisplayName("주문 번호로 결제 정보 조회 테스트")
    void givenOrderNumber_whenGetOrderInfoPayment_thenReturnBookOrderPaymentInfoResponse() throws Exception {
        BookOrderPaymentInfoRespones respones =
                new BookOrderPaymentInfoRespones("test", "test@test.com", "010-1111-1111",
                        "test", "test", "test status");

        when(bookOrderService.getOrderInfoPayment(anyString())).thenReturn(respones);
        mockMvc.perform(get(url + "/info/{orderNumber}/pay", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1111-1111"))
                .andExpect(jsonPath("$.orderNumber").value("test"))
                .andExpect(jsonPath("$.orderName").value("test"))
                .andExpect(jsonPath("$.orderStatus").value("test status"))
                .andDo(document("book-order-find-pay-info-by-orderNumber",
                        pathParameters(
                                parameterWithName("orderNumber").description("주문 번호")
                        ),
                        responseFields(
                                fieldWithPath("name").description("구매자 이름"),
                                fieldWithPath("email").description("구매자 이메일"),
                                fieldWithPath("phoneNumber").description("구매자 번호"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("orderName").description("주문 이름"),
                                fieldWithPath("orderStatus").description("주문 상태")
                        )
                ));
    }


}
