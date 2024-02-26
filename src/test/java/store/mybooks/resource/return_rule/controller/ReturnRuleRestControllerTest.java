package store.mybooks.resource.return_rule.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
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
@WebMvcTest(ReturnRuleRestControllerTest.class)
class ReturnRuleRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReturnRuleService returnRuleService;

    @Test
    void getReturnRule() {

    }

    @Test
    void getReturnRuleList() {
    }

    @Test
    void createReturnRule() {
    }

    @Test
    void modifyReturnRule() {
    }

    @Test
    void deleteReturnRule() {
    }
}