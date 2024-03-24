package store.mybooks.resource.pointrulename.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.pointrulename.dto.request.PointRuleNameCreateRequest;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameCreateResponse;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameResponse;
import store.mybooks.resource.pointrulename.service.PointRuleNameService;

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
        mockMvc.perform(get("/api/point-rule-names/{id}", "test"))
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
    void given_whenGetPointRuleNameList_thenReturnPointRuleNameResponseList() throws Exception {
        List<PointRuleNameResponse> pointRuleNameResponseList = List.of(
                new PointRuleNameResponse("test12")
        );
        when(pointRuleNameService.getPointRuleNameList()).thenReturn(pointRuleNameResponseList);

        mockMvc.perform(get("/api/point-rule-names"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("test12"))
                .andDo(document("point-rule-name-list",
                        responseFields(
                                fieldWithPath("[].id").description("포인트 규정")
                        )));
    }

    @Test
    @DisplayName("포인트 규정 명 생성 테스트")
    void givenPointRuleNameCreateRequest_whenCreatePointRuleName_thenReturnPointRuleNameCreateResponse() throws Exception {
        PointRuleNameCreateRequest request = new PointRuleNameCreateRequest();
        ReflectionTestUtils.setField(request, "id", "test12");
        PointRuleNameCreateResponse response = new PointRuleNameCreateResponse("test12");
        when(pointRuleNameService.createPointRuleName(any())).thenReturn(response);

        mockMvc.perform(post("/api/point-rule-names")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("test12"))
                .andDo(document("point-rule-name-create",
                        requestFields(
                                fieldWithPath("id").description("포인트 규정 명")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포인트 규정 명")
                        )));
        verify(pointRuleNameService, times(1)).createPointRuleName(any());
    }

    @Test
    @DisplayName("포인트 규정 명 생성 시 유효성 검사 테스트")
    void givenPointRuleNameCreateRequest_whenUtilsValidateRequest_thenReturnBadRequest() throws Exception {
        PointRuleNameCreateRequest request = new PointRuleNameCreateRequest();
        ReflectionTestUtils.setField(request, "id", "");
        mockMvc.perform(post("/api/point-rule-names")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(pointRuleNameService, never()).createPointRuleName(any());
    }
}