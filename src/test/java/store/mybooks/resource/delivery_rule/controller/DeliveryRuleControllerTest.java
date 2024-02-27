package store.mybooks.resource.delivery_rule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.service.DeliveryRuleService;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;

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
@WebMvcTest(value = DeliveryRuleController.class,excludeAutoConfiguration = SecurityAutoConfiguration.class)
class DeliveryRuleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRuleService deliveryRuleService;

    @Test
    @DisplayName("DeliveryRule read 테스트")
    void givenDeliveryRule_whenGetDeliveryRule_thenReturnDeliveryRuleDto() throws Exception {
        DeliveryRuleDto deliveryRuleDto = new DeliveryRuleDto() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public String getDeliveryRuleName() {
                return "test";
            }

            @Override
            public String getCompanyName() {
                return "test";
            }

            @Override
            public Integer getCost() {
                return 123;
            }

            @Override
            public Integer getRuleCost() {
                return 123;
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }

            @Override
            public Integer getIsAvailable() {
                return 1;
            }
        };

        when(deliveryRuleService.getDeliveryRule(1)).thenReturn(deliveryRuleDto);

        mockMvc.perform(get("/api/delivery-rules/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryRuleDto.getId()))
                .andExpect(jsonPath("$.companyName").value(deliveryRuleDto.getCompanyName()))
                .andExpect(jsonPath("$.cost").value(deliveryRuleDto.getCost()))
                .andExpect(jsonPath("$.ruleCost").value(deliveryRuleDto.getRuleCost()))
                .andExpect(jsonPath("$.createdDate").value(deliveryRuleDto.getCreatedDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleDto.getIsAvailable()));

        verify(deliveryRuleService, times(1)).getDeliveryRule(1);
    }

    @Test
    @DisplayName("DeliveryRule 등록하는 테스트")
    void givenDeliveryRuleRegisterRequest_whenRegisterDeliveryRule_thenCreateDeliveryRuleReturnDeliveryRuleResponse()
            throws Exception {
        DeliveryRuleName deliveryNameRule = new DeliveryRuleName("test");
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test2", 123, 123, LocalDate.now(), 1);
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
    @DisplayName("DeliveryRule 수정 테스트")
    void givenDeliveryRuleIdAndDeliveryRuleModifyRequest_whenModifyDeliveryRule_thenModifyDeliveryRuleReturnDeliveryRuleResponse()
            throws Exception {

        DeliveryRuleName deliveryNameRule = new DeliveryRuleName("test");
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, "test2", 123, 123, LocalDate.now(), 1);
        DeliveryRuleModifyRequest deliveryRuleModifyRequest = new DeliveryRuleModifyRequest("test", "test2", 123, 123);
        when(deliveryRuleService.modifyDeliveryRule(any(), any())).thenReturn(deliveryRuleResponse);

        mockMvc.perform(
                        put("/api/delivery-rules/{id}", 1).content(
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

        verify(deliveryRuleService, times(1)).modifyDeliveryRule(any(), any());
    }

    @Test
    @DisplayName("DeliveryRule 삭제 테스트")
    void givenDeliveryId_whenDeleteDeliveryRule_thenDeleteDeliveryRuleReturnNothing() throws Exception {
        mockMvc.perform(delete("/api/delivery-rules/{id}", 1))
                .andExpect(status().isOk());

        verify(deliveryRuleService, times(1)).deleteDeliveryRule(any());
    }

}