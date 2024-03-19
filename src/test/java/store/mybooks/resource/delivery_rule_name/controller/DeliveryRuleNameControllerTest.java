package store.mybooks.resource.delivery_rule_name.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import store.mybooks.resource.delivery_rule_name.dto.request.DeliveryRuleNameRegisterRequest;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_rule_name.service.DeliveryRuleNameService;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.controller
 * fileName       : DeliveryNameRuleControllerTest
 * author         : Fiat_lux
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        Fiat_lux       최초 생성
 */

@WebMvcTest(value = DeliveryRuleNameController.class)
class DeliveryRuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRuleNameService deliveryRuleNameService;

    @Test
    @DisplayName("배송 규칙 이름 list 조회")
    void given_whenGetDeliveryNameRuleList_thenReturnDeliveryRuleNameResponseList() throws Exception {
        List<DeliveryRuleNameResponse> deliveryRuleNameResponseList =
                List.of(new DeliveryRuleNameResponse("test", LocalDate.of(2023, 12, 31)));

        when(deliveryRuleNameService.getDeliveryNameRuleList()).thenReturn(deliveryRuleNameResponseList);
        mockMvc.perform(get("/api/delivery-name-rules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(deliveryRuleNameResponseList.get(0).getId()))
                .andExpect(jsonPath("$[0].createdDate").value(deliveryRuleNameResponseList.get(0).getCreatedDate().toString()));
    }

    @Test
    @DisplayName("배송 규칙 이름 조회")
    void givenDeliveryRuleNameId_whenFindDeliveryRuleId_thenReturnDeliveryRuleNameDto() throws Exception {
        DeliveryRuleNameDto deliveryNameRuleDto = new DeliveryRuleNameDto() {
            @Override
            public String getId() {
                return "test";
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }
        };

        when(deliveryRuleNameService.getDeliveryNameRule(any())).thenReturn(deliveryNameRuleDto);

        mockMvc.perform(get("/api/delivery-name-rules/{id}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryNameRuleDto.getId()))
                .andExpect(jsonPath("$.createdDate").value(deliveryNameRuleDto.getCreatedDate().toString()));

        verify(deliveryRuleNameService, times(1)).getDeliveryNameRule(any());
    }

    @Test
    @DisplayName("배송 규칙 이름 생성")
    void givenDeliveryRuleNameRegisterRequest_whenCreateDeliveryRuleName_thenSaveDeliveryRuleNameAndReturnDeliveryRuleNameResponse()
            throws Exception {
        DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest = new DeliveryRuleNameRegisterRequest("test");
        DeliveryRuleNameResponse deliveryRuleNameResponse = new DeliveryRuleNameResponse("test", LocalDate.now());

        when(deliveryRuleNameService.registerDeliveryNameRule(any())).thenReturn(
                deliveryRuleNameResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery-name-rules")
                        .content(new ObjectMapper().writeValueAsString(deliveryRuleNameRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryRuleNameResponse.getId()))
                .andExpect(jsonPath("$.createdDate").value(deliveryRuleNameResponse.getCreatedDate().toString()));
        verify(deliveryRuleNameService, times(1)).registerDeliveryNameRule(any());
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 id값의 유효성을 지키지 않은 경우")
    void givenDeliveryRuleNameRegisterRequest_whenRegisterDeliveryNameRuleIdIsBlank_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest = new DeliveryRuleNameRegisterRequest("");
        mockMvc.perform(post("/api/delivery-name-rules").content(
                                new ObjectMapper().writeValueAsString(deliveryRuleNameRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(deliveryRuleNameService, never()).registerDeliveryNameRule(any());
    }

    @Test
    @DisplayName("post 요청으로 들어온 데이터의 id값의 유효성을 지키지 않은 경우 - max size 50")
    void givenDeliveryRuleNameRegisterRequest_whenRegisterDeliveryNameRuleIdMaxSize50_thenHttpStatusIsBadRequest()
            throws Exception {
        DeliveryRuleNameRegisterRequest deliveryRuleNameRegisterRequest =
                new DeliveryRuleNameRegisterRequest("qwertyuiopasdfghjklzxcvbnmqwertyuiiiiiioasdfghjklzxcv");
        mockMvc.perform(post("/api/delivery-name-rules").content(
                                new ObjectMapper().writeValueAsString(deliveryRuleNameRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(deliveryRuleNameService, never()).registerDeliveryNameRule(any());
    }

    @Test
    @DisplayName("배송 규칙 이름 삭제")
    void givenDeliveryNameRuleId_whenDeleteDeliveryNameRule_thenDeleteDeliveryNameRuleAndReturnNoting()
            throws Exception {
        mockMvc.perform(delete("/api/delivery-name-rules/{id}", "test"))
                .andExpect(status().isOk());

        verify(deliveryRuleNameService, times(1)).deleteDeliveryNameRule(any());
    }
}