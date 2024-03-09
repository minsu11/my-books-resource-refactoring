package store.mybooks.resource.point_rule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.point_rule.dto.response.PointRuleResponse;
import store.mybooks.resource.point_rule.service.PointRuleService;

/**
 * packageName    : store.mybooks.resource.point_rule.controller<br>
 * fileName       : PointRuleControllerTest<br>
 * author         : minsu11<br>
 * date           : 3/8/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/8/24        minsu11       최초 생성<br>
 */
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(PointRuleController.class)
class PointRuleControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PointRuleService pointRuleService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("id로 포인트 규정 조회")
    void givenId_whenGetPointRuleResponse_thenReturnPointRuleResponse() throws Exception {
        Integer id = 1;

        PointRuleResponse pointRuleResponse = new PointRuleResponse(1, "test", 10, null);
        when(pointRuleService.getPointRuleResponse(any())).thenReturn(pointRuleResponse);

        mockMvc.perform(get("/api/point-rules/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pointRuleName").value("test"))
                .andExpect(jsonPath("$.rate").value(10))
                .andDo(document("point-rule",
                                pathParameters(
                                        parameterWithName("id").description("조회할 포인트 규정 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("포인트 규정 아이디"),
                                        fieldWithPath("pointRuleName").description("포인트 규정 명"),
                                        fieldWithPath("rate").description("포인트 적립 률"),
                                        fieldWithPath("cost").description("포인트 적립 금액")
                                )
                        )
                );
        verify(pointRuleService, times(1)).getPointRuleResponse(any());

    }

    @Test
    @DisplayName("포인트 규정 전체 조회")
    void whenGetPointRuleList_thenReturnPointRuleResponseList() throws Exception {
        List<PointRuleResponse> pointRuleResponseList = List.of(new PointRuleResponse(1, "test", 10, null));
        when(pointRuleService.getPointRuleList()).thenReturn(pointRuleResponseList);

        mockMvc.perform(get("/api/point-rules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pointRuleName").value("test"))
                .andExpect(jsonPath("$[0].rate").value(10))
                .andDo(document("point-rule-list",
                        responseFields(
                                fieldWithPath("[].id").description("포인트 규정 아이디"),
                                fieldWithPath("[].pointRuleName").description("포인트 규정 이름"),
                                fieldWithPath("[].rate").description("포인트 적립률"),
                                fieldWithPath("[].cost").description("포인트 적립금")
                        )));
        verify(pointRuleService, times(1)).getPointRuleList();
    }

    @Test
    void getPointRuleResponsePage() {
    }

    @Test
    void createPointRuleResponse() {
    }

    @Test
    void modifyPointRuleResponse() {
    }

    @Test
    void deletePointRuleResponse() {
    }
}