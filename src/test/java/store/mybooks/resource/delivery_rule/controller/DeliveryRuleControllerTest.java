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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleDto;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleResponse;
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
@WebMvcTest(DeliveryRuleController.class)
class DeliveryRuleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRuleService deliveryRuleService;

    @Test
    void getDeliveryRuleTest() throws Exception {
        DeliveryRuleDto deliveryRuleDto = new DeliveryRuleDto() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public Integer getDeliveryNameRuleId() {
                return 1;
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
            public Boolean getIsAvailable() {
                return true;
            }
        };

        when(deliveryRuleService.getDeliveryRule(1)).thenReturn(deliveryRuleDto);

        mockMvc.perform(get("/api/deliveryRules/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryRuleDto.getId()))
                .andExpect(jsonPath("$.deliveryNameRuleId").value(deliveryRuleDto.getDeliveryNameRuleId()))
                .andExpect(jsonPath("$.companyName").value(deliveryRuleDto.getCompanyName()))
                .andExpect(jsonPath("$.cost").value(deliveryRuleDto.getCost()))
                .andExpect(jsonPath("$.ruleCost").value(deliveryRuleDto.getRuleCost()))
                .andExpect(jsonPath("$.createdDate").value(deliveryRuleDto.getCreatedDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(deliveryRuleDto.getIsAvailable()));

        verify(deliveryRuleService, times(1)).getDeliveryRule(1);
    }

    @Test
    void createDeliveryRuleTest() throws Exception {
        DeliveryNameRule deliveryNameRule = new DeliveryNameRule(1, "test", LocalDate.now());
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, deliveryNameRule, "test2", 123, 123, LocalDate.now(), true);
        DeliveryRuleRegisterRequest deliveryRuleRegisterRequest = new DeliveryRuleRegisterRequest(1, "test2", 123, 123);
        when(deliveryRuleService.registerDeliveryRule(any())).thenReturn(deliveryRuleResponse);

        mockMvc.perform(
                        post("/api/deliveryRules").content(new ObjectMapper().writeValueAsString(deliveryRuleRegisterRequest))
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
    void modifyDeliveryRuleTest() throws Exception {

        DeliveryNameRule deliveryNameRule = new DeliveryNameRule(1, "test", LocalDate.now());
        DeliveryRuleResponse deliveryRuleResponse =
                new DeliveryRuleResponse(1, deliveryNameRule, "test2", 123, 123, LocalDate.now(), true);
        DeliveryRuleModifyRequest deliveryRuleModifyRequest = new DeliveryRuleModifyRequest(1, "test2", 123, 123);
        when(deliveryRuleService.modifyDeliveryRule(any(), any())).thenReturn(deliveryRuleResponse);

        mockMvc.perform(
                        put("/api/deliveryRules/1").content(new ObjectMapper().writeValueAsString(deliveryRuleModifyRequest))
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
    void deleteDeliveryRuleTest() throws Exception {
        mockMvc.perform(delete("/api/deliveryRules/1")).andExpect(status().isOk());

        verify(deliveryRuleService, times(1)).deleteDeliveryRule(any());
    }

}