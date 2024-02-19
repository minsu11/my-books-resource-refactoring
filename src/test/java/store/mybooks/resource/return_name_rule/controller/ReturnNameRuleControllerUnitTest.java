package store.mybooks.resource.return_name_rule.controller;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.mybooks.resource.return_name_rule.dto.response.ReturnNameRuleResponse;
import store.mybooks.resource.return_name_rule.service.ReturnNameRuleService;

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
    ReturnNameRuleController returnNameRuleController;

    @Mock
    ReturnNameRuleService returnNameRuleService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(returnNameRuleController).build();
    }

    @Test
    void givenStringName_whenCallGetReturnNameRule_thenReturnReturnNameRuleResponse() throws Exception {
        ReturnNameRuleResponse response = new ReturnNameRuleResponse("test123", LocalDate.now());
        when(returnNameRuleService.getReturnNameRule("test123")).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/return-name-rule/{name}", "test123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", equalTo(response.getName())));


    }
}