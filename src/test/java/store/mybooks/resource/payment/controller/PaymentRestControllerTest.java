package store.mybooks.resource.payment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.bookorder.service.TotalOrderService;
import store.mybooks.resource.payment.dto.request.PayCancelRequest;
import store.mybooks.resource.payment.dto.request.PayCreateRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.dto.response.PaymentResponse;
import store.mybooks.resource.payment.service.PaymentService;

/**
 * packageName    : store.mybooks.resource.payment.controller<br>
 * fileName       : PaymentRestControllerTest<br>
 * author         : minsu11<br>
 * date           : 3/26/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/26/24        minsu11       최초 생성<br>
 */
@Import(PaymentRestControllerTest.TestConfig.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(PaymentRestController.class)
class PaymentRestControllerTest {
    @Autowired
    private PaymentService paymentService;

    private MockMvc mockMvc;

    @Autowired
    private TotalOrderService totalOrderService;

    private final String url = "/api/pays";

    @Autowired
    ObjectMapper objectMapper;

    private Long userId = 1L;

    @TestConfiguration
    static class TestConfig{
        @Bean
        TotalOrderService totalOrderService(){
            return mock(TotalOrderService.class);
        }

        @Bean
        PaymentService paymentService(){
            return mock(PaymentService.class);
        }
    }


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("비회원 결제 처리 테스트")
    void givenPayCreateRequest_whenPayUser_thenReturnPayCreateResponse() throws Exception {
        PayCreateResponse response = new PayCreateResponse(1L, "toss payment key", 10);
        PayCreateRequest request = new PayCreateRequest();
        ReflectionTestUtils.setField(request, "orderNumber", "testOrderNumber");
        ReflectionTestUtils.setField(request, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(request, "status", "test status");
        ReflectionTestUtils.setField(request, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(request, "totalAmount", 1000);
        ReflectionTestUtils.setField(request, "method", "test method");

        when(totalOrderService.payUser(any(), anyLong())).thenReturn(response);

        mockMvc.perform(post(url + "/non/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.payId").value(response.getPayId()))
                .andExpect(jsonPath("$.paymentKey").value(response.getPaymentKey()))
                .andExpect(jsonPath("$.totalAmount").value(response.getTotalAmount()))
                .andDo(document("payment-non-user-created",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 요청된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 응답한 method")
                        ),
                        responseFields(
                                fieldWithPath("payId").description("저장된 결제 내역 아이디"),
                                fieldWithPath("paymentKey").description("저장된 토스페이먼츠 키"),
                                fieldWithPath("totalAmount").description("저장된 결제 금액")
                        )));
        verify(totalOrderService, times(1)).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(주문 번호 null)")
    void givenPayCreateRequest_whenCreatePayment() throws Exception {
        PayCreateRequest orderNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(orderNullRequest, "orderNumber", null);
        ReflectionTestUtils.setField(orderNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(orderNullRequest, "status", "test status");
        ReflectionTestUtils.setField(orderNullRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(orderNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(orderNullRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNullRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-id-null-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호가 null"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(주문 번호 빈값)")
    void givenPayCreateRequestOrderNumberEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest orderNumberEmptyRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "orderNumber", "");
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "status", "test status");
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumberEmptyRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-orderNumber-empty-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호가 빈값"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }


    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(주문 번호 공백)")
    void givenPayCreateRequestOrderNumberBlank_whenCreatePayment() throws Exception {
        PayCreateRequest orderNumberBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(orderNumberBlankRequest, "orderNumber", " ");
        ReflectionTestUtils.setField(orderNumberBlankRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(orderNumberBlankRequest, "status", "test status");
        ReflectionTestUtils.setField(orderNumberBlankRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(orderNumberBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(orderNumberBlankRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumberBlankRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-orderNumber-blank-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호가 공백"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(주문 번호 20자리 넘어감)")
    void givenPayCreateRequestOrderNumberMaxOverSize_whenCreatePayment() throws Exception {
        PayCreateRequest orderNumberMaxOverSizeRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "orderNumber", "test123451sdsadawqqwqfasasfasfwqasda");
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "status", "test status");
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumberMaxOverSizeRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-orderNumber-max-size-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호가 20자리 넘어감"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(토스 페이먼츠 null)")
    void givenPayCreateRequestTossPaymentKeyNull_whenCreatePayment() throws Exception {
        PayCreateRequest tossPaymentKeyNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "paymentKey", null);
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "status", "test status");
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tossPaymentKeyNullRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-toss-null-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키가 null"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(토스 페이먼츠 빈값)")
    void givenPayCreateRequestTossPaymentKeyEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest tossPaymentKeyEmptyRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "paymentKey", "");
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "status", "test status");
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tossPaymentKeyEmptyRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-toss-empty-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키가 빈값"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(토스 페이먼츠 공백)")
    void givenPayCreateRequestTossPaymentKeyBlank_whenCreatePayment() throws Exception {
        PayCreateRequest tossPaymentKeyBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "paymentKey", " ");
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "status", "test status");
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tossPaymentKeyBlankRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-toss-blank-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키가 공백"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(토스 페이먼츠 50자리 넘어감)")
    void givenPayCreateRequestTossPaymentKeyMaxOverSize_whenCreatePayment() throws Exception {
        PayCreateRequest tossPaymentKeyMaxOverSizeRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "paymentKey", "testtesttesttesttesttesttesttesttesttesttesttest" +
                "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest" +
                "testtesttesttesttesttesttesttesttesttesttest");
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "status", "test status");
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tossPaymentKeyMaxOverSizeRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-toss-max-over-size-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키가 50자리 넘어감"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(status Null)")
    void givenPayCreateRequestStatusNull_whenCreatePayment() throws Exception {
        PayCreateRequest statusNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(statusNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(statusNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(statusNullRequest, "status", null);
        ReflectionTestUtils.setField(statusNullRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(statusNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(statusNullRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusNullRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-status-null-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태가 null"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(status Blank)")
    void givenPayCreateRequestKeyStatusBlank_whenCreatePayment() throws Exception {
        PayCreateRequest statusBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(statusBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(statusBlankRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(statusBlankRequest, "status", " ");
        ReflectionTestUtils.setField(statusBlankRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(statusBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(statusBlankRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusBlankRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-status-blank-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태가 공백"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(status 빈 값)")
    void givenPayCreateRequestStatusEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest statusEmptyRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(statusEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(statusEmptyRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(statusEmptyRequest, "status", "");
        ReflectionTestUtils.setField(statusEmptyRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(statusEmptyRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(statusEmptyRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusEmptyRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-status-empty-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태가 빈 값"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(status 20자리 넘어감)")
    void givenPayCreateRequestStatusMaxOverSize_whenCreatePayment() throws Exception {
        PayCreateRequest statusMaxOverSizeRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusMaxOverSizeRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-status-max-over-size-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태가 20자리 넘어감"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(결제 승인 시간 null인 경우)")
    void givenPayCreateRequestRequestedAtNull_whenCreatePayment() throws Exception {
        PayCreateRequest requestedAtNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(requestedAtNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(requestedAtNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(requestedAtNullRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(requestedAtNullRequest, "requestedAt", null);
        ReflectionTestUtils.setField(requestedAtNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(requestedAtNullRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestedAtNullRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-requestedAt-null-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간이 null"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(결제 승인 시간 Empty인 경우)")
    void givenPayCreateRequestRequestedAtEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest requestedAtNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(requestedAtNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(requestedAtNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(requestedAtNullRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(requestedAtNullRequest, "requestedAt", "");
        ReflectionTestUtils.setField(requestedAtNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(requestedAtNullRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestedAtNullRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-requestedAt-empty-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간이 null"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(결제 승인 시간 공백인 경우)")
    void givenPayCreateRequestRequestedAtBlank_whenCreatePayment() throws Exception {
        PayCreateRequest requestedAtBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(requestedAtBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(requestedAtBlankRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(requestedAtBlankRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(requestedAtBlankRequest, "requestedAt", " ");
        ReflectionTestUtils.setField(requestedAtBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(requestedAtBlankRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestedAtBlankRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-requestAt-blank-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간이 공백"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(총액 null인 경우)")
    void givenPayCreateRequestTotalAmountNull_whenCreatePayment() throws Exception {
        PayCreateRequest totalAmountNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(totalAmountNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(totalAmountNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(totalAmountNullRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(totalAmountNullRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(totalAmountNullRequest, "totalAmount", null);
        ReflectionTestUtils.setField(totalAmountNullRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalAmountNullRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-total-cost-null-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액 null"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(총액 음수인 경우)")
    void givenPayCreateRequestTotalAmountNegativeNum_whenCreatePayment() throws Exception {
        PayCreateRequest totalAmountNegativeNumRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "totalAmount", -1);
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalAmountNegativeNumRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-total-cost-negative-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액 음수"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(총액 max 값 넘어간 경우)")
    void givenPayCreateRequestTotalAmountMaxOver_whenCreatePayment() throws Exception {
        PayCreateRequest totalAmountMaxOverNumRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "totalAmount", Integer.MAX_VALUE);
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalAmountMaxOverNumRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-total-cost-max-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액 최댓값 넘어간 경우"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(method null인 경우)")
    void givenPayCreateRequestMethodNull_whenCreatePayment() throws Exception {
        PayCreateRequest methodNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(methodNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(methodNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(methodNullRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(methodNullRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(methodNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(methodNullRequest, "method", null);


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(methodNullRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-method-null-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method가 null")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(method 빈 값 인 경우)")
    void givenPayCreateRequestMethodEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest methodEmptyRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(methodEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(methodEmptyRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(methodEmptyRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(methodEmptyRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(methodEmptyRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(methodEmptyRequest, "method", "");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(methodEmptyRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-method-empty-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method가 빈값")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(method 공백인 경우)")
    void givenPayCreateRequestMethodBlank_whenCreatePayment() throws Exception {
        PayCreateRequest methodBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(methodBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(methodBlankRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(methodBlankRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(methodBlankRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(methodBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(methodBlankRequest, "method", " ");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(methodBlankRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-non-user-method-blank-validation-fail",
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method가 공백")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }


    @Test
    @DisplayName("회원 결제 처리 테스트")
    void givenPayCreateRequestUser_whenPayUser_thenReturnPayCreateResponse() throws Exception {
        PayCreateResponse response = new PayCreateResponse(1L, "toss payment key", 10);
        PayCreateRequest request = new PayCreateRequest();
        ReflectionTestUtils.setField(request, "orderNumber", "testOrderNumber");
        ReflectionTestUtils.setField(request, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(request, "status", "test status");
        ReflectionTestUtils.setField(request, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(request, "totalAmount", 1000);
        ReflectionTestUtils.setField(request, "method", "test method");

        when(totalOrderService.payUser(any(), anyLong())).thenReturn(response);

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", userId))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.payId").value(response.getPayId()))
                .andExpect(jsonPath("$.paymentKey").value(response.getPaymentKey()))
                .andExpect(jsonPath("$.totalAmount").value(response.getTotalAmount()))
                .andDo(document("payment-user-created",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 요청된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 응답한 method")
                        ),
                        responseFields(
                                fieldWithPath("payId").description("저장된 결제 내역 아이디"),
                                fieldWithPath("paymentKey").description("저장된 토스페이먼츠 키"),
                                fieldWithPath("totalAmount").description("저장된 결제 금액")
                        )));
        verify(totalOrderService, times(1)).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(주문 번호 null)")
    void givenPayCreateRequestUser_whenCreatePayment() throws Exception {
        PayCreateRequest orderNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(orderNullRequest, "orderNumber", null);
        ReflectionTestUtils.setField(orderNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(orderNullRequest, "status", "test status");
        ReflectionTestUtils.setField(orderNullRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(orderNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(orderNullRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNullRequest))
                        .header("X-User-Id", userId)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-orderNumber-null-validation-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호가 null"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(주문 번호 빈값)")
    void givenPayCreateRequestUserOrderNumberEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest orderNumberEmptyRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "orderNumber", "");
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "status", "test status");
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(orderNumberEmptyRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumberEmptyRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-orderNumber-empty-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호가 빈값"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }


    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(주문 번호 공백)")
    void givenPayCreateRequestUserOrderNumberBlank_whenCreatePayment() throws Exception {
        PayCreateRequest orderNumberBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(orderNumberBlankRequest, "orderNumber", "");
        ReflectionTestUtils.setField(orderNumberBlankRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(orderNumberBlankRequest, "status", "test status");
        ReflectionTestUtils.setField(orderNumberBlankRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(orderNumberBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(orderNumberBlankRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumberBlankRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-orderNumber-blank-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호가 공백"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(주문 번호 20자리 넘어감)")
    void givenPayCreateRequestOrderUserNumberMaxOverSize_whenCreatePayment() throws Exception {
        PayCreateRequest orderNumberMaxOverSizeRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "orderNumber", "test123451sdsadawqqwqfasasfasfwqasda");
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "status", "test status");
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(orderNumberMaxOverSizeRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumberMaxOverSizeRequest))
                        .header("X-User-Id", userId)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-orderNumber-max-over-size-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호가 20자리 넘어감"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(토스 페이먼츠 null)")
    void givenPayCreateRequestUserTossPaymentKeyNull_whenCreatePayment() throws Exception {
        PayCreateRequest tossPaymentKeyNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "paymentKey", null);
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "status", "test status");
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(tossPaymentKeyNullRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tossPaymentKeyNullRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-toss-null-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키가 null"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(토스 페이먼츠 빈값)")
    void givenPayCreateRequestUserTossPaymentKeyEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest tossPaymentKeyEmptyRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "paymentKey", "");
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "status", "test status");
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(tossPaymentKeyEmptyRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tossPaymentKeyEmptyRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-toss-empty-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키가 빈값"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(토스 페이먼츠 공백)")
    void givenPayCreateRequestUserTossPaymentKeyBlank_whenCreatePayment() throws Exception {
        PayCreateRequest tossPaymentKeyBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "paymentKey", " ");
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "status", "test status");
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(tossPaymentKeyBlankRequest, "method", "test method");


        mockMvc.perform(post(url + "/non/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tossPaymentKeyBlankRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-toss-blank-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키가 공백"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(토스 페이먼츠 50자리 넘어감)")
    void givenPayCreateRequestUserTossPaymentKeyMaxOverSize_whenCreatePayment() throws Exception {
        PayCreateRequest tossPaymentKeyMaxOverSizeRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "paymentKey", "testtesttesttesttesttesttesttesttesttesttesttest" +
                "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest" +
                "testtesttesttesttesttesttesttesttesttesttest");
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "status", "test status");
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(tossPaymentKeyMaxOverSizeRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tossPaymentKeyMaxOverSizeRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-toss-max-over-size-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키가 50자리 넘어감"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(status Null)")
    void givenPayCreateRequestUserStatusNull_whenCreatePayment() throws Exception {
        PayCreateRequest statusNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(statusNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(statusNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(statusNullRequest, "status", null);
        ReflectionTestUtils.setField(statusNullRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(statusNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(statusNullRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusNullRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-status-null-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태가 null"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(status Blank)")
    void givenPayCreateRequestUserKeyStatusBlank_whenCreatePayment() throws Exception {
        PayCreateRequest statusBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(statusBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(statusBlankRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(statusBlankRequest, "status", " ");
        ReflectionTestUtils.setField(statusBlankRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(statusBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(statusBlankRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusBlankRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-status-blank-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태가 공백"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(status 빈 값)")
    void givenPayCreateRequestUserStatusEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest statusEmptyRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(statusEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(statusEmptyRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(statusEmptyRequest, "status", "");
        ReflectionTestUtils.setField(statusEmptyRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(statusEmptyRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(statusEmptyRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusEmptyRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-status-empty-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태가 빈 값"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(status 20자리 넘어감)")
    void givenPayCreateRequestUserStatusMaxOverSize_whenCreatePayment() throws Exception {
        PayCreateRequest statusMaxOverSizeRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "requestedAt", "2022-01-03");
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(statusMaxOverSizeRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusMaxOverSizeRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-status-max-over-size-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태가 20자리 넘어감"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(결제 승인 시간 null인 경우)")
    void givenPayCreateRequestUserRequestedAtNull_whenCreatePayment() throws Exception {
        PayCreateRequest requestedAtNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(requestedAtNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(requestedAtNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(requestedAtNullRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(requestedAtNullRequest, "requestedAt", null);
        ReflectionTestUtils.setField(requestedAtNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(requestedAtNullRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestedAtNullRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-requestAt-null-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간이 null"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(결제 승인 시간 Empty인 경우)")
    void givenPayCreateRequestUserRequestedAtEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest requestedAtNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(requestedAtNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(requestedAtNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(requestedAtNullRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(requestedAtNullRequest, "requestedAt", "");
        ReflectionTestUtils.setField(requestedAtNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(requestedAtNullRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestedAtNullRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-requestAt-empty-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간이 null"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(결제 승인 시간 공백인 경우)")
    void givenPayCreateRequestUserRequestedAtBlank_whenCreatePayment() throws Exception {
        PayCreateRequest requestedAtBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(requestedAtBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(requestedAtBlankRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(requestedAtBlankRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(requestedAtBlankRequest, "requestedAt", " ");
        ReflectionTestUtils.setField(requestedAtBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(requestedAtBlankRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestedAtBlankRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-requestAt-blank-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간이 공백"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 결제 처리 유효성 실패 검사(총액 null인 경우)")
    void givenPayCreateRequestUserTotalAmountNull_whenCreatePayment() throws Exception {
        PayCreateRequest totalAmountNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(totalAmountNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(totalAmountNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(totalAmountNullRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(totalAmountNullRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(totalAmountNullRequest, "totalAmount", null);
        ReflectionTestUtils.setField(totalAmountNullRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalAmountNullRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-total-cost-null-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액 null"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(총액 음수인 경우)")
    void givenPayCreateRequestUserTotalAmountNegativeNum_whenCreatePayment() throws Exception {
        PayCreateRequest totalAmountNegativeNumRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "totalAmount", -1);
        ReflectionTestUtils.setField(totalAmountNegativeNumRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalAmountNegativeNumRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-total-cost-negative-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액 음수"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(총액 max 값 넘어간 경우)")
    void givenPayCreateRequestUserTotalAmountMaxOver_whenCreatePayment() throws Exception {
        PayCreateRequest totalAmountMaxOverNumRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "totalAmount", Integer.MAX_VALUE);
        ReflectionTestUtils.setField(totalAmountMaxOverNumRequest, "method", "test method");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalAmountMaxOverNumRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-total-cost-max-over-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액 최댓값 넘어간 경우"),
                                fieldWithPath("method").description("토스에서 보내는 method")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(method null인 경우)")
    void givenPayCreateRequestUserMethodNull_whenCreatePayment() throws Exception {
        PayCreateRequest methodNullRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(methodNullRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(methodNullRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(methodNullRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(methodNullRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(methodNullRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(methodNullRequest, "method", null);


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(methodNullRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-method-null-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method가 null")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(method 빈 값 인 경우)")
    void givenPayCreateRequestUserMethodEmpty_whenCreatePayment() throws Exception {
        PayCreateRequest methodEmptyRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(methodEmptyRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(methodEmptyRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(methodEmptyRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(methodEmptyRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(methodEmptyRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(methodEmptyRequest, "method", "");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(methodEmptyRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-method-empty-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method가 빈값")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }

    @Test
    @DisplayName("회원 결제 처리 유효성 실패 검사(method 공백인 경우)")
    void givenPayCreateRequestUserMethodBlank_whenCreatePayment() throws Exception {
        PayCreateRequest methodBlankRequest = new PayCreateRequest();
        ReflectionTestUtils.setField(methodBlankRequest, "orderNumber", "test");
        ReflectionTestUtils.setField(methodBlankRequest, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(methodBlankRequest, "status", "test status test status test");
        ReflectionTestUtils.setField(methodBlankRequest, "requestedAt", "2020-01-01");
        ReflectionTestUtils.setField(methodBlankRequest, "totalAmount", 1000);
        ReflectionTestUtils.setField(methodBlankRequest, "method", " ");


        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(methodBlankRequest))
                        .header("X-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andDo(document("payment-user-method-blank-validation-test-fail",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("status").description("토스에서 보낸 결제 상태"),
                                fieldWithPath("requestedAt").description("토스에서 승인 된 시간"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("method").description("토스에서 보내는 method가 공백")
                        )));
        verify(totalOrderService, never()).payUser(any(), anyLong());
    }


    @Test
    @DisplayName("주문 번호로 결제 내역 조회")
    void givenOrderNumber_whenGetPaymentKey_thenReturnPaymentResponse() throws Exception {
        PaymentResponse response = new PaymentResponse("toss payment key");
        when(paymentService.getPaymentKey(anyString())).thenReturn(response);

        mockMvc.perform(get(url + "/{orderNumber}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paymentKey").value(response.getPaymentKey()))
                .andDo(document("order-number-get-payment",
                        pathParameters(
                                parameterWithName("orderNumber").description("주문 번호")
                        ),
                        responseFields(
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키")
                        )
                ));
    }

    @Test
    @DisplayName("결제 취소")
    void givenPayCancelRequest_whenCancelOrderProcess_thenReturnVoid() throws Exception {
        PayCancelRequest request = new PayCancelRequest();
        ReflectionTestUtils.setField(request, "paymentKey", "toss payment key");
        ReflectionTestUtils.setField(request, "orderNumber", "orderNumber");
        ReflectionTestUtils.setField(request, "status", "status");
        ReflectionTestUtils.setField(request, "totalAmount", 1000);
        ReflectionTestUtils.setField(request, "requestedAt", "2024-03-13");


        mockMvc.perform(post(url + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andDo(document("payment-cancel",
                        requestFields(
                                fieldWithPath("paymentKey").description("토스 페이먼츠 키"),
                                fieldWithPath("orderNumber").description("주문 번호"),
                                fieldWithPath("status").description("결제 상태"),
                                fieldWithPath("totalAmount").description("결제 금액"),
                                fieldWithPath("requestedAt").description("결제 승인 날짜")
                        )));

    }
}