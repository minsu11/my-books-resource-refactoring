package store.mybooks.resource.point_rule_name.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.point_rule_name.dto.response.PointRuleNameResponse;
import store.mybooks.resource.point_rule_name.service.PointRuleNameService;

/**
 * packageName    : store.mybooks.resource.point_rule_name.controller<br>
 * fileName       : PointRuleNameRestControllerTest<br>
 * author         : minsu11<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/7/24        minsu11       최초 생성<br>
 */

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(PointRuleNameRestController.class)
class PointRuleNameRestControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PointRuleNameService pointRuleNameService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("id로 포인트 규정 명 조회")
    void givenId_whenGetPointRuleName_thenReturnPointRuleNameResponse() throws Exception {
        PointRuleNameResponse pointRuleNameResponse = new PointRuleNameResponse("test");
        when(pointRuleNameService.getPointRuleName(any())).thenReturn(pointRuleNameResponse);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/point-rule-names/{id}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("test"))
                .andDo(document("point-rule-name-find",
                        pathParameters(
                                parameterWithName("id").description("포인트 규정 명")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포인트 규정 명")
                        )));


        verify(pointRuleNameService, times(1)).getPointRuleName(any());

    }

    @Test
    @DisplayName("포인트 규정 명 전체 조회")
    void getPointRuleNameList() {
    }

    @Test
    void createPointRuleName() {
    }
}