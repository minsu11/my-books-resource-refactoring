package store.mybooks.resource.return_rule_name.controller;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import store.mybooks.resource.return_rule_name.dto.request.ReturnRuleNameCreateRequest;
import store.mybooks.resource.return_rule_name.dto.response.ReturnRuleNameCreateResponse;
import store.mybooks.resource.return_rule_name.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.return_rule_name.service.ReturnRuleNameService;

/**
 * packageName    : store.mybooks.resource.return_name_rule.controller<br>
 * fileName       : ReturnNameRuleControllerTest<br>
 * author         : minsu11<br>
 * date           : 2/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/20/24        minsu11       최초 생성<br>
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = ReturnRuleNameRestController.class)
class ReturnRuleNameControllerUnitTest {


    @MockBean
    ReturnRuleNameService returnRuleNameService;

    @Autowired
    MockMvc mockMvc;
    @MockBean
    BindingResult bindingResult;


    @Test
    @Order(1)
    @DisplayName("id값에 대한 반품 규정 명 조회 데이터를 response 응답")
    void givenStringName_whenCallGetReturnNameRule_thenReturnReturnNameRuleResponse() throws Exception {
        ReturnRuleNameResponse response = new ReturnRuleNameResponse("test123", LocalDate.of(2024, Month.APRIL, 2));
        when(returnRuleNameService.getReturnRuleName("test123")).thenReturn(response);
        mockMvc.perform(get("/api/return-rule-names/{id}", "test123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(response.getId())));

        verify(returnRuleNameService, times(1)).getReturnRuleName(anyString());

    }

    @Test
    @Order(2)
    @DisplayName("모든 반품 규정 명에 대한 조회 데이터를 response list 응답")
    void given_whenGetReturnRuleNameList_thenReturnReturnRuleNameResponseList() throws Exception {
        LocalDate date = LocalDate.parse("1212-12-12");
        List<ReturnRuleNameResponse> returnRuleNameResponseList = List.of(new ReturnRuleNameResponse("test", date));
        when(returnRuleNameService.getReturnRuleNameList()).thenReturn(returnRuleNameResponseList);
        mockMvc.perform(get("/api/return-rule-names"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", equalTo(returnRuleNameResponseList.size())))
                .andExpect(jsonPath("$[0].id", equalTo(returnRuleNameResponseList.get(0).getId())))
                .andExpect(jsonPath("$[0].createdDate").value("1212-12-12"));
        verify(returnRuleNameService, times(1)).getReturnRuleNameList();
    }

    @Test
    @Order(3)
    @DisplayName("DB에 아무런 값이 없을 경우 빈 리스트를 응답")
    void given_whenGetReturnRuleNameList_thenReturnEmptyList() throws Exception {
        when(returnRuleNameService.getReturnRuleNameList()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/return-rule-names"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0));
        verify(returnRuleNameService, times(1)).getReturnRuleNameList();
    }

    @Test
    @Order(4)
    @DisplayName("요청 데이터를 저장한 뒤 response dto 반환")
    void givenReturnRuleNameCreateRequest_whenCreateReturnRuleName_thenReturnReturnRulenameCreateResponse()
            throws Exception {
        String testData = "test123";
        ReturnRuleNameCreateRequest request = new ReturnRuleNameCreateRequest(testData);
        LocalDate date = LocalDate.of(1212, 12, 12);
        ReturnRuleNameCreateResponse response = new ReturnRuleNameCreateResponse(testData, date);
        ObjectMapper objectMapper = new ObjectMapper();

        when(returnRuleNameService.createReturnRuleName(any())).thenReturn(response);

        mockMvc.perform(post("/api/return-rule-names")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testData))
                .andExpect(jsonPath("$.createdDate", equalTo("1212-12-12")));
        verify(returnRuleNameService, times(1)).createReturnRuleName(any());
    }

    @Test
    @Order(5)
    @DisplayName("요청으로 들어온 데이터가 유효성을 지키지 않은 경우")
    void givenReturnNameCreateRequest_whenCreateReturnRuleName_thenReturnReturnRuleNameCreateResponse()
            throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ReturnRuleNameCreateRequest request = new ReturnRuleNameCreateRequest("");
        mockMvc.perform(post("/api/return_rule_names")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}