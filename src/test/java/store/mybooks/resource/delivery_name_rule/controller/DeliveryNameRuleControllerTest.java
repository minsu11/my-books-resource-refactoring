package store.mybooks.resource.delivery_name_rule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleDto;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleModifyRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleRegisterRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleResponse;
import store.mybooks.resource.delivery_name_rule.service.DeliveryNameRuleService;

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
@WebMvcTest(DeliveryNameRuleController.class)
class DeliveryNameRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryNameRuleService deliveryNameRuleService;

    @Test
    void getDeliveryNameRuleTest() throws Exception {
        DeliveryNameRuleDto deliveryNameRuleDto = new DeliveryNameRuleDto() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }
        };

        when(deliveryNameRuleService.getDeliveryNameRule(1)).thenReturn(deliveryNameRuleDto);

        mockMvc.perform(get("/api/deliveryNameRules/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryNameRuleDto.getId()))
                .andExpect(jsonPath("$.name").value(deliveryNameRuleDto.getName()))
                .andExpect(jsonPath("$.createdDate").value(deliveryNameRuleDto.getCreatedDate().toString()));

        verify(deliveryNameRuleService, times(1)).getDeliveryNameRule(1);
    }

    @Test
    void createDeliveryNameRuleTest() throws Exception {
        DeliveryNameRuleRegisterRequest deliveryNameRuleRegisterRequest = new DeliveryNameRuleRegisterRequest("test");
        DeliveryNameRuleResponse deliveryNameRuleResponse = new DeliveryNameRuleResponse();
        deliveryNameRuleResponse.setId(1);
        deliveryNameRuleResponse.setName("test");
        deliveryNameRuleResponse.setCreatedDate(LocalDate.now());

        when(deliveryNameRuleService.registerDeliveryNameRule(any())).thenReturn(
                deliveryNameRuleResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deliveryNameRules")
                        .content(new ObjectMapper().writeValueAsString(deliveryNameRuleRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryNameRuleResponse.getId()))
                .andExpect(jsonPath("$.name").value(deliveryNameRuleResponse.getName()))
                .andExpect(jsonPath("$.createdDate").value(deliveryNameRuleResponse.getCreatedDate().toString()));
        verify(deliveryNameRuleService, times(1)).registerDeliveryNameRule(any());
    }

    @Test
    void modifyDeliveryNameRuleTest() throws Exception {
        DeliveryNameRuleResponse expected = new DeliveryNameRuleResponse();
        expected.setId(1);
        expected.setName("test");
        expected.setCreatedDate(LocalDate.now());

        DeliveryNameRuleModifyRequest deliveryNameRuleModifyRequest = new DeliveryNameRuleModifyRequest("test");
        when(deliveryNameRuleService.modifyDeliveryNameRule(1, deliveryNameRuleModifyRequest)).thenReturn(expected);

        mockMvc.perform(put("/api/deliveryNameRules/1")
                        .content(new ObjectMapper().writeValueAsString(deliveryNameRuleModifyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.createdDate").value(expected.getCreatedDate().toString()));

        verify(deliveryNameRuleService, times(1)).modifyDeliveryNameRule(1, deliveryNameRuleModifyRequest);
    }

    @Test
    void deleteDeliveryNameRuleTest() throws Exception {
        mockMvc.perform(delete("/api/deliveryNameRules/1"))
                .andExpect(status().isOk());

        verify(deliveryNameRuleService, times(1)).deleteDeliveryNameRule(1);
    }


}