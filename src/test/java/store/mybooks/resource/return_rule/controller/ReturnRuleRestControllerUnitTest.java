package store.mybooks.resource.return_rule.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.return_rule.dto.request.ReturnRuleCreateRequest;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleCreateResponse;
import store.mybooks.resource.return_rule.dto.response.ReturnRuleResponse;
import store.mybooks.resource.return_rule.service.ReturnRuleService;

/**
 * packageName    : store.mybooks.resource.return_rule.controller<br>
 * fileName       : ReturnRuleRestControllerTest<br>
 * author         : minsu11<br>
 * date           : 2/26/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/26/24        minsu11       최초 생성<br>
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ReturnRuleRestController.class)
class ReturnRuleRestControllerUnitTest {

    @Autowired
    MockMvc mockMvc;


    @MockBean
    ReturnRuleService returnRuleService;

    @Test
    @DisplayName("반품 규정 id 조회 성공 테스트")
    void givenStringReturnRuleId_whenGetReturnRuleResponseByReturnRuleName_thenReturnReturnRuleResponse() throws Exception {
        String name = "test";
        ReturnRuleResponse response = new ReturnRuleResponse("test", 1000, 10, true);
        when(returnRuleService.getReturnRuleResponseByReturnRuleName(name)).thenReturn(response);

        mockMvc.perform(get("/api/return-rules/{id}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.returnName").value("test"))
                .andExpect(jsonPath("$.deliveryFee").value(1000))
                .andExpect(jsonPath("$.term").value(10))
                .andExpect(jsonPath("$.isAvailable").value(true));
        verify(returnRuleService, times(1)).getReturnRuleResponseByReturnRuleName(any());
    }

    @Test
    @DisplayName("전체 반품 규정 조회 성공 테스트")
    void given_whenGetReturnRuleResponseList_thenReturnReturnRuleResponseList() throws Exception {

        when(returnRuleService.getReturnRuleResponseList()).thenReturn(List.of(new ReturnRuleResponse("test", 1000, 10, true)));
        mockMvc.perform(get("/api/return-rules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].returnName").value("test"))
                .andExpect(jsonPath("$[0].deliveryFee").value(1000))
                .andExpect(jsonPath("$[0].term").value(10))
                .andExpect(jsonPath("$[0].isAvailable").value(true));
        verify(returnRuleService, times(1)).getReturnRuleResponseList();

    }

    @Test
    @DisplayName("DB에 저장된 반품 규정이 없을 경우 빈 리스트 반환")
    void given_whenGetReturnRuleResponseList_thenReturnEmptyList() throws Exception {
        when(returnRuleService.getReturnRuleResponseList()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/return-rules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0));
        verify(returnRuleService, times(1)).getReturnRuleResponseList();
    }

    @Test
    @DisplayName("반품 규정 등록 성공 테스트")
    void givenReturnRuleCreateRequest_whenCreateReturnRule_thenReturnReturnRuleCreateResponse() throws Exception {
        ReturnRuleCreateResponse response = new ReturnRuleCreateResponse("test", 100, 10, Boolean.TRUE);
        ObjectMapper mapper = new ObjectMapper();
        ReturnRuleCreateRequest request = new ReturnRuleCreateRequest("test", 100, 10);
        when(returnRuleService.createReturnRule(request)).thenReturn(response);

        mockMvc.perform(post("/api/return-rules")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.returnName").value("test"));

    }

    @Test
    void modifyReturnRule() {
    }

    @Test
    void deleteReturnRule() {
    }
}