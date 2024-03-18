package store.mybooks.resource.delivery_rule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
@WebMvcTest(value = DeliveryRuleController.class)
class DeliveryRuleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRuleService deliveryRuleService;

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
                .andExpect(jsonPath("$[0].isAvailable").value(expected.get(0).getIsAvailable()));
    }

    @Test
    @DisplayName("DeliveryRule read 테스트")
    void givenDeliveryRule_whenGetDeliveryRule_thenReturnDeliveryRuleDto() throws Exception {
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test", "hi", 123, 123, LocalDate.now(), 1);

        when(deliveryRuleService.getDeliveryRule(1)).thenReturn(deliveryRuleResponse);

        mockMvc.perform(get("/api/delivery-rules/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryRuleResponse.getId()))
                .andExpect(jsonPath("$.deliveryRuleNameId").value(deliveryRuleResponse.getDeliveryRuleNameId()))
                .andExpect(jsonPath("$.companyName").value(deliveryRuleResponse.getCompanyName()))
                .andExpect(jsonPath("$.cost").value(deliveryRuleResponse.getCost()))
                .andExpect(jsonPath("$.ruleCost").value(deliveryRuleResponse.getRuleCost()))
                .andExpect(jsonPath("$.createdDate").value(deliveryRuleResponse.getCreatedDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleResponse.getIsAvailable()));

        verify(deliveryRuleService, times(1)).getDeliveryRule(1);
    }

    @Test
    @DisplayName("DeliveryRule 등록하는 테스트")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryRule_thenCreateDeliveryRuleReturnDeliveryRuleResponse()
            throws Exception {
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test", "test2", 123, 123, LocalDate.now(), 1);
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "test2", 123, 123);
        when(deliveryRuleService.registerDeliveryRule(any())).thenReturn(deliveryRuleResponse);

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
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleResponse.getIsAvailable()));
        verify(deliveryRuleService, times(1)).registerDeliveryRule(any());
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 id값의 유효성을 지키지 않은 경우")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryRuleIdIsBlank_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("", "test", 123, 123);
        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(deliveryRuleService, never()).registerDeliveryRule(any());
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 외래키의 유효성을 지키지 않은 경우")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryRuleDeliveryCompanyNameIsBlank_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest =
                new DeliveryRuleRegisterRequest("test", "", 123, 123);
        mockMvc.perform(
                        post("/api/delivery-rules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(deliveryRuleService, never()).registerDeliveryRule(any());
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
                .andDo(print());

        verify(deliveryRuleService, never()).registerDeliveryRule(any());
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
                .andDo(print());

        verify(deliveryRuleService, never()).registerDeliveryRule(any());
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
                .andDo(print());

        verify(deliveryRuleService, never()).registerDeliveryRule(any());
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
                .andDo(print());

        verify(deliveryRuleService, never()).registerDeliveryRule(any());
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
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleResponse.getIsAvailable()));

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
                .andDo(print());

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
                .andDo(print());

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
                .andDo(print());

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
                .andDo(print());

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
                .andDo(print());

        verify(deliveryRuleService, never()).modifyDeliveryRule(any(DeliveryRuleModifyRequest.class));
    }


    @Test
    @DisplayName("DeliveryRule 삭제 테스트")
    void givenDeliveryId_whenDeleteDeliveryRule_thenDeleteDeliveryRuleReturnNothing() throws Exception {
        mockMvc.perform(delete("/api/delivery-rules/{id}", 1))
                .andExpect(status().isOk());

        verify(deliveryRuleService, times(1)).deleteDeliveryRule(any());
    }

}