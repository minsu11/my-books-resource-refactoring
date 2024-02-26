package store.mybooks.resource.delivery_rule_name.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import store.mybooks.resource.delivery_rule_name.dto.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.dto.DeliveryRuleNameRegisterRequest;
import store.mybooks.resource.delivery_rule_name.dto.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_rule_name.service.DeliveryRuleNameService;
import store.mybooks.resource.delivery_rule_name.controller.DeliveryRuleNameController;

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
@WebMvcTest(DeliveryRuleNameController.class)
class DeliveryRuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRuleNameService deliveryRuleNameService;

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
        DeliveryRuleNameResponse deliveryRuleNameResponse = new DeliveryRuleNameResponse();
        deliveryRuleNameResponse.setId("test");
        deliveryRuleNameResponse.setCreatedDate(LocalDate.now());

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
    @DisplayName("배송 규칙 이름 삭제")
    void givenDeliveryNameRuleId_whenDeleteDeliveryNameRule_thenDeleteDeliveryNameRuleAndReturnNoting()
            throws Exception {
        mockMvc.perform(delete("/api/delivery-name-rules/{id}", "test"))
                .andExpect(status().isOk());

        verify(deliveryRuleNameService, times(1)).deleteDeliveryNameRule(any());
    }
}