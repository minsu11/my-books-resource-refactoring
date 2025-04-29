package store.mybooks.resource.delivery_rule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.delivery_rule.dto.request.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.request.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.response.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.service.DeliveryRuleService;

/**
 * packageName    : store.mybooks.resource.delivery_rule.controller
 * fileName       : DeliveryRuleControllerTest
 * author         : Fiat_lux
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        Fiat_lux       최초 생성
 */
@Import(DeliveryRuleControllerTest.TestConfig.class)
@WebMvcTest(value = DeliveryRuleController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class DeliveryRuleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeliveryRuleService deliveryRuleService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        DeliveryRuleService deliveryRuleService() {
            return mock(DeliveryRuleService.class);
        }
    }

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
    @DisplayName("DeliveryRule List read 테스트")
    void given_whenGetDeliveryRuleList_thenReturnStatusIsOkAndBodyDeliveryRUleResponseList() throws Exception {
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test", "hi", 123, 123, LocalDate.now(), 1);
        List<DeliveryRuleResponse> expected = List.of(deliveryRuleResponse);
        when(deliveryRuleService.getDeliveryRuleList()).thenReturn(expected);

        mockMvc.perform(get("/api/delivery-rules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(expected.get(0).getId()))
                .andExpect(jsonPath("$[0].deliveryRuleNameId").value(expected.get(0).getDeliveryRuleNameId()))
                .andExpect(jsonPath("$[0].companyName").value(expected.get(0).getCompanyName()))
                .andExpect(jsonPath("$[0].cost").value(expected.get(0).getCost()))
                .andExpect(jsonPath("$[0].ruleCost").value(expected.get(0).getRuleCost()))
                .andExpect(jsonPath("$[0].createdDate").value(expected.get(0).getCreatedDate().toString()))
                .andExpect(jsonPath("$[0].isAvailable").value(expected.get(0).getIsAvailable()))
                .andDo(document("delivery-rule-get-list",
                        responseFields(
                                fieldWithPath("[].id").description("배송 규정 아이디"),
                                fieldWithPath("[].deliveryRuleNameId").description("배송 규정 명 아이디"),
                                fieldWithPath("[].companyName").description("회사 이름"),
                                fieldWithPath("[].cost").description("가격"),
                                fieldWithPath("[].ruleCost").description("가격 규칙"),
                                fieldWithPath("[].createdDate").description("생성일"),
                                fieldWithPath("[].isAvailable").description("활성화 되었는지")
                        )));
    }

    @Test
    @DisplayName("DeliveryRule name으로 조회 테스트")
    void givenDeliveryRuleName_whenGetDeliveryRule_thenReturnDeliveryRuleResponse() throws Exception {
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test", "hi", 123, 123, LocalDate.now(), 1);
        when(deliveryRuleService.getDeliveryRuleByName(any(String.class))).thenReturn(deliveryRuleResponse);

        mockMvc.perform(get("/api/delivery-rules/name/{name}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryRuleResponse.getId()))
                .andExpect(jsonPath("$.deliveryRuleNameId").value(deliveryRuleResponse.getDeliveryRuleNameId()))
                .andExpect(jsonPath("$.companyName").value(deliveryRuleResponse.getCompanyName()))
                .andExpect(jsonPath("$.cost").value(deliveryRuleResponse.getCost()))
                .andExpect(jsonPath("$.ruleCost").value(deliveryRuleResponse.getRuleCost()))
                .andExpect(jsonPath("$.createdDate").value(deliveryRuleResponse.getCreatedDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleResponse.getIsAvailable()))
                .andDo(document("delivery-rule-get-byName",
                        pathParameters(parameterWithName("name").description("조회할 배송 규칙 name")),
                        responseFields(
                                fieldWithPath("id").description("배송 규정 아이디"),
                                fieldWithPath("deliveryRuleNameId").description("배송 규정 명 아이디"),
                                fieldWithPath("companyName").description("회사 이름"),
                                fieldWithPath("cost").description("가격"),
                                fieldWithPath("ruleCost").description("가격 규칙"),
                                fieldWithPath("createdDate").description("생성일"),
                                fieldWithPath("isAvailable").description("활성화 되었는지")
                        )));
    }

    @Test
    @DisplayName("DeliveryRule read 테스트")
    void givenDeliveryRule_whenGetDeliveryRule_thenReturnDeliveryRuleDto() throws Exception {
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test", "hi", 123, 123, LocalDate.now(), 1);

        when(deliveryRuleService.getDeliveryRule(any(Integer.class))).thenReturn(deliveryRuleResponse);

        mockMvc.perform(get("/api/delivery-rules/{deliveryRuleId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryRuleResponse.getId()))
                .andExpect(jsonPath("$.deliveryRuleNameId").value(deliveryRuleResponse.getDeliveryRuleNameId()))
                .andExpect(jsonPath("$.companyName").value(deliveryRuleResponse.getCompanyName()))
                .andExpect(jsonPath("$.cost").value(deliveryRuleResponse.getCost()))
                .andExpect(jsonPath("$.ruleCost").value(deliveryRuleResponse.getRuleCost()))
                .andExpect(jsonPath("$.createdDate").value(deliveryRuleResponse.getCreatedDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleResponse.getIsAvailable()))
                .andDo(document("delivery-rule-get-byId",
                        pathParameters(parameterWithName("deliveryRuleId").description("조회할 배송 규칙 ID")),
                        responseFields(
                                fieldWithPath("id").description("배송 규정 아이디"),
                                fieldWithPath("deliveryRuleNameId").description("배송 규정 명 아이디"),
                                fieldWithPath("companyName").description("회사 이름"),
                                fieldWithPath("cost").description("가격"),
                                fieldWithPath("ruleCost").description("가격 규칙"),
                                fieldWithPath("createdDate").description("생성일"),
                                fieldWithPath("isAvailable").description("활성화 되었는지")
                        )));

        verify(deliveryRuleService, times(1)).getDeliveryRule(any(Integer.class));
    }

    @Test
    @DisplayName("DeliveryRule 등록하는 테스트")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryRule_thenCreateDeliveryRuleReturnDeliveryRuleResponse()
            throws Exception {
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test", "test2", 123, 123, LocalDate.now(), 1);
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "test2", 123, 123);
        when(deliveryRuleService.registerDeliveryRule(any(DeliveryRuleRegisterRequest.class))).thenReturn(
                deliveryRuleResponse);

        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryRuleResponse.getId()))
                .andExpect(jsonPath("$.companyName").value(deliveryRuleResponse.getCompanyName()))
                .andExpect(jsonPath("$.cost").value(deliveryRuleResponse.getCost()))
                .andExpect(jsonPath("$.ruleCost").value(deliveryRuleResponse.getRuleCost()))
                .andExpect(jsonPath("$.createdDate").value(deliveryRuleResponse.getCreatedDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleResponse.getIsAvailable()))
                .andDo(document("delivery-rule-create",
                        requestFields(
                                fieldWithPath("deliveryNameRuleId").description("배송 규정 명 아이디"),
                                fieldWithPath("deliveryRuleCompanyName").description("회사 이름"),
                                fieldWithPath("deliveryCost").description("가격"),
                                fieldWithPath("deliveryRuleCost").description("가격 규칙")
                        ),
                        responseFields(
                                fieldWithPath("id").description("배송 규정 아이디"),
                                fieldWithPath("deliveryRuleNameId").description("배송 규정 명 아이디"),
                                fieldWithPath("companyName").description("회사 이름"),
                                fieldWithPath("cost").description("가격"),
                                fieldWithPath("ruleCost").description("가격 규칙"),
                                fieldWithPath("createdDate").description("생성일"),
                                fieldWithPath("isAvailable").description("활성화 되었는지")
                        )));
        verify(deliveryRuleService, times(1)).registerDeliveryRule(any(DeliveryRuleRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 외래키의 유효성을 지키지 않은 경우")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryRuleIdIsBlank_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("", "test", 123, 123);
        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-create-error-deliveryNameRuleId-notBlank"));

        verify(deliveryRuleService, never()).registerDeliveryRule(any(DeliveryRuleRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 회사 이름 필드의 유효성을 지키지 않은 경우")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryRuleDeliveryCompanyNameIsBlank_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "", 123, 123);
        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-create-error-deliveryRuleCompanyName-notBlank"));

        verify(deliveryRuleService, never()).registerDeliveryRule(any(DeliveryRuleRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 길이가 넘어가는 경우 - deliveryNameRuleId")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryNameRuleIdMaxSize50_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("qwertyuiopasdfghjklzxcvbnmqwertyuiiiiiioasdfghjklzxcv", "test", 123,
                        123);
        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-create-error-deliveryNameRuleId-maxSize50"));

        verify(deliveryRuleService, never()).registerDeliveryRule(any(DeliveryRuleRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 길이가 넘어가는 경우 - deliveryRuleCompanyName")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryCompanyNameMaxSize20_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "qwertyuiopasdfghjklzxc", 123, 123);
        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-create-error-deliveryRuleCompanyName-maxSize20"));

        verify(deliveryRuleService, never()).registerDeliveryRule(any(DeliveryRuleRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 값의 최솟값이 해당 안되는 경우 - deliveryCost")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryCostMin0_thenHttpStatusIsBadRequest() throws Exception {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "test", -1, 123);
        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-create-error-deliveryCost-min0"));

        verify(deliveryRuleService, never()).registerDeliveryRule(any(DeliveryRuleRegisterRequest.class));
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 값의 최솟값이 해당 안되는 경우 - deliveryRuleCost")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryRuleCostMin0_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "test", 123, -1);
        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-create-error-deliveryRuleCost-min0"));

        verify(deliveryRuleService, never()).registerDeliveryRule(any(DeliveryRuleRegisterRequest.class));
    }


    @Test
    @DisplayName("DeliveryRule 수정 테스트")
    void givenDeliveryRuleIdAndDeliveryRuleModifyRequest_whenModifyDeliveryRule_thenModifyDeliveryRuleReturnDeliveryRuleResponse()
            throws Exception {

        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test", "test2", 123, 123, LocalDate.now(), 1);
        DeliveryRuleModifyRequest deliveryRuleModifyRequest = new DeliveryRuleModifyRequest(1, "test2", 123, 123);
        when(deliveryRuleService.modifyDeliveryRule(any(DeliveryRuleModifyRequest.class))).thenReturn(
                deliveryRuleResponse);

        mockMvc.perform(
                        put("/api/delivery-rules/modify").content(
                                        new ObjectMapper().writeValueAsString(deliveryRuleModifyRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deliveryRuleResponse.getId()))
                .andExpect(jsonPath("$.companyName").value(deliveryRuleResponse.getCompanyName()))
                .andExpect(jsonPath("$.cost").value(deliveryRuleResponse.getCost()))
                .andExpect(jsonPath("$.ruleCost").value(deliveryRuleResponse.getRuleCost()))
                .andExpect(jsonPath("$.createdDate").value(deliveryRuleResponse.getCreatedDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleResponse.getIsAvailable()))
                .andDo(document("delivery-rule-modify",
                        requestFields(
                                fieldWithPath("id").description("배송 규정 아이디"),
                                fieldWithPath("deliveryRuleCompanyName").description("회사 이름"),
                                fieldWithPath("deliveryCost").description("가격"),
                                fieldWithPath("deliveryRuleCost").description("가격 규칙")
                        ),
                        responseFields(
                                fieldWithPath("id").description("배송 규정 아이디"),
                                fieldWithPath("deliveryRuleNameId").description("배송 규정 명 아이디"),
                                fieldWithPath("companyName").description("회사 이름"),
                                fieldWithPath("cost").description("가격"),
                                fieldWithPath("ruleCost").description("가격 규칙"),
                                fieldWithPath("createdDate").description("생성일"),
                                fieldWithPath("isAvailable").description("활성화 되었는지")
                        )));

        verify(deliveryRuleService, times(1)).modifyDeliveryRule(any(DeliveryRuleModifyRequest.class));
    }

    @Test
    @DisplayName("put 요청으로 들어온 데이터의 id값의 유효성을 지키지 않은 경우")
    void givenDeliveryRuleModifyRequest_whenModifyDeliveryRuleIdIsBlank_thenHttpStatusIsBadRequest() throws Exception {
        DeliveryRuleModifyRequest deliveryRuleRegisterRequest =
                new DeliveryRuleModifyRequest(0, "test", 123, 123);
        mockMvc.perform(
                        put("/api/delivery-rules/modify").content(
                                        new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-modify-error-id-min0"));

        verify(deliveryRuleService, never()).modifyDeliveryRule(any(DeliveryRuleModifyRequest.class));
    }

    @Test
    @DisplayName("put 요청으로 들어온 데이터의 외래키의 유효성을 지키지 않은 경우")
    void givenDeliveryRuleModifyRequest_whenModifyDeliveryRUleCompanyNameIsBlank_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleModifyRequest deliveryRuleRegisterRequest =
                new DeliveryRuleModifyRequest(1, "", 123, 123);
        mockMvc.perform(
                        put("/api/delivery-rules/modify").content(
                                        new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-modify-error-deliveryRuleCompanyName-notBlank"));

        verify(deliveryRuleService, never()).modifyDeliveryRule(any(DeliveryRuleModifyRequest.class));
    }

    @Test
    @DisplayName("put 요청으로 들어온 데이터의 길이가 넘어가는 경우 - deliveryNameRuleId")
    void givenDeliveryRuleModifyRequest_whenRegisterDeliveryNameRuleIdMaxSize50_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleModifyRequest deliveryRuleRegisterRequest =
                new DeliveryRuleModifyRequest(1, "qwertyuiopasdfghjklzxcvbnmqwertyuiiiiiioasdfghjklzxcv", 123, 123);
        mockMvc.perform(
                        put("/api/delivery-rules/modify").content(
                                        new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-modify-error-deliveryRuleCompanyName-maxSize20"));

        verify(deliveryRuleService, never()).modifyDeliveryRule(any(DeliveryRuleModifyRequest.class));
    }

    @Test
    @DisplayName("put 요청으로 들어온 데이터의 값의 최솟값이 해당 안되는 경우 - deliveryCost")
    void givenDeliveryRuleModifyRequest_whenRegisterDeliveryCostMin0_thenHttpStatusIsBadRequest() throws Exception {
        DeliveryRuleModifyRequest deliveryRuleModifyRequest =
                new DeliveryRuleModifyRequest(1, "test", -1, 123);
        mockMvc.perform(
                        put("/api/delivery-rules/modify").content(
                                        new ObjectMapper().writeValueAsString(deliveryRuleModifyRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-modify-deliveryCost-min0"));

        verify(deliveryRuleService, never()).modifyDeliveryRule(any(DeliveryRuleModifyRequest.class));
    }

    @Test
    @DisplayName("put 요청으로 들어온 데이터의 값의 최솟값이 해당 안되는 경우 - deliveryRuleCost")
    void givenDeliveryRuleModifyRequest_whenRegisterDeliveryRuleCostMin0_thenHttpStatusIsBadRequest() throws Exception {
        DeliveryRuleModifyRequest deliveryRuleModifyRequest =
                new DeliveryRuleModifyRequest(1, "test", 123, -1);
        mockMvc.perform(
                        put("/api/delivery-rules/modify").content(
                                        new ObjectMapper().writeValueAsString(deliveryRuleModifyRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("delivery-rule-modify-deliveryRuleCost-min0"));

        verify(deliveryRuleService, never()).modifyDeliveryRule(any(DeliveryRuleModifyRequest.class));
    }


    @Test
    @DisplayName("DeliveryRule 삭제 테스트")
    void givenDeliveryId_whenDeleteDeliveryRule_thenDeleteDeliveryRuleReturnNothing() throws Exception {
        mockMvc.perform(delete("/api/delivery-rules/{deliveryRuleId}", 1))
                .andExpect(status().isOk())
                .andDo(document("delivery-rule-delete",
                        pathParameters(
                                parameterWithName("deliveryRuleId").description("배송 규칙 ID")
                        )));

        verify(deliveryRuleService, times(1)).deleteDeliveryRule(any(Integer.class));
    }

}