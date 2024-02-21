package store.mybooks.resource.return_name_rule.controller;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.mybooks.resource.return_rule_name.controller.ReturnRuleNameRestController;
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
class ReturnNameRuleControllerUnitTest {

    @InjectMocks
    ReturnRuleNameRestController returnRuleNameRestController;

    @Mock
    ReturnRuleNameService returnRuleNameService;


    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(returnRuleNameRestController).build();
    }


    @Test
    void givenStringName_whenCallGetReturnNameRule_thenReturnReturnNameRuleResponse() throws Exception {
        ReturnRuleNameResponse response = new ReturnRuleNameResponse("test", LocalDate.of(2024, Month.APRIL, 2));
        when(returnRuleNameService.getReturnRuleName("test123")).thenReturn(response);
        mockMvc.perform(get("/api/return-name-rule/{id}", "test123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(response.getId())));

    }

    @Test
    void given_whenGetReturnRuleNameList_thenReturnReturnRuleNameResponseList() throws Exception {
        LocalDate date = LocalDate.parse("1212-12-12");
        List<ReturnRuleNameResponse> returnRuleNameResponseList = List.of(new ReturnRuleNameResponse("test", date));
        when(returnRuleNameService.getReturnRuleNameList()).thenReturn(returnRuleNameResponseList);
        mockMvc.perform(get("/api/return-name-rule"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", equalTo(returnRuleNameResponseList.size())))
                .andExpect(jsonPath("$[0].id", equalTo(returnRuleNameResponseList.get(0).getId())))
                .andExpect(jsonPath("$[0].createdDate[0]").value("1212"))
                .andExpect(jsonPath("$[0].createdDate[1]").value("12"))
                .andExpect(jsonPath("$[0].createdDate[2]").value("12"));
    }
}