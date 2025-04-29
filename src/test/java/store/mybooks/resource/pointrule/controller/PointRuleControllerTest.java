package store.mybooks.resource.pointrule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.pointrule.dto.request.PointRuleCreateRequest;
import store.mybooks.resource.pointrule.dto.request.PointRuleModifyRequest;
import store.mybooks.resource.pointrule.dto.response.PointRuleCreateResponse;
import store.mybooks.resource.pointrule.dto.response.PointRuleModifyResponse;
import store.mybooks.resource.pointrule.dto.response.PointRuleResponse;
import store.mybooks.resource.pointrule.service.PointRuleService;

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
@Import(PointRuleControllerTest.TestContextConfiguration.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(PointRuleController.class)
class PointRuleControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PointRuleService pointRuleService;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        PointRuleService pointRuleService() {
            return mock(PointRuleService.class);
        }
    }


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
    @DisplayName("포인트 규정 전체 조회 페이징 처리")
    void whenGetPointRuleResponsePage_thenReturnPointRuleResponsePage() throws Exception {
        List<PointRuleResponse> pointRuleResponseList = List.of(new PointRuleResponse(1, "test", 10, null));
        long total = pointRuleResponseList.size();
        Pageable pageable = PageRequest.of(0, 3);
        Page<PointRuleResponse> pointRulePage = new PageImpl<>(pointRuleResponseList, pageable, total);

        when(pointRuleService.getPointRuleResponsePage(any())).thenReturn(pointRulePage);

        mockMvc.perform(get("/api/point-rules/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageable)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].pointRuleName").value("test"))
                .andExpect(jsonPath("$.content[0].rate").value(10))
                .andDo(document("point-rule-pagination",
                        requestFields(
                                fieldWithPath("pageNumber").description("페이지"),
                                fieldWithPath("pageSize").description("사이즈"),
                                fieldWithPath("sort.*").ignored(),
                                fieldWithPath("offset").ignored(),
                                fieldWithPath("paged").ignored(),
                                fieldWithPath("unpaged").ignored()
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].id").description("포인트 아이디"),
                                fieldWithPath("content[].pointRuleName").description("포인트 규정 명"),
                                fieldWithPath("content[].rate").description("적립 률"),
                                fieldWithPath("content[].cost").description("적립 금액"),
                                fieldWithPath("pageable.sort.*").ignored(),
                                fieldWithPath("pageable.*").ignored(),
                                fieldWithPath("last").ignored(),
                                fieldWithPath("totalElements").ignored(),
                                fieldWithPath("totalPages").ignored(),
                                fieldWithPath("number").ignored(),
                                fieldWithPath("size").ignored(),
                                fieldWithPath("first").ignored(),
                                fieldWithPath("numberOfElements").ignored(),
                                fieldWithPath("sort.*").ignored(),
                                fieldWithPath("empty").ignored())
                ));
        verify(pointRuleService, times(1)).getPointRuleResponsePage(any());

    }

    @Test
    @DisplayName("포인트 규정 등록")
    void givenPointRuleCreateRequest_whenCreatePointRuleResponse_thenReturnPointCreateResponse() throws Exception {
        PointRuleCreateResponse response = new PointRuleCreateResponse("test12", 10, null);
        PointRuleCreateRequest request = new PointRuleCreateRequest();
        ReflectionTestUtils.setField(request, "pointRuleName", "test12");
        ReflectionTestUtils.setField(request, "rate", 10);
        ReflectionTestUtils.setField(request, "cost", null);
        when(pointRuleService.createPointRuleResponse(any())).thenReturn(response);

        mockMvc.perform(post("/api/point-rules")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pointRuleName").value("test12"))
                .andExpect(jsonPath("$.rate").value(10))
                .andDo(document("point-rule-created",
                        requestFields(
                                fieldWithPath("pointRuleName").description("포인트 규정 명"),
                                fieldWithPath("rate").description("포인트 적립 률"),
                                fieldWithPath("cost").description("포인트 적립 금액")
                        ),
                        responseFields(
                                fieldWithPath("pointRuleName").description("포인트 규정 명"),
                                fieldWithPath("rate").description("포인트 적립 률"),
                                fieldWithPath("cost").description("포인트 적립 금액")
                        )
                ));
        verify(pointRuleService, times(1)).createPointRuleResponse(any());
    }

    @Test
    @DisplayName("포인트 등록 시 유효성 검사 실패")
    void givenPointRuleCreateRequest_whenCreatePointRuleResponse_thenReturnIsBadRequest() throws Exception {
        PointRuleCreateRequest request = new PointRuleCreateRequest();
        ReflectionTestUtils.setField(request, "pointRuleName", "12");
        ReflectionTestUtils.setField(request, "rate", null);
        ReflectionTestUtils.setField(request, "cost", null);
        mockMvc.perform(post("/api/point-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(pointRuleService, never()).createPointRuleResponse(any());

    }

    @Test
    @DisplayName("포인트 규정 수정")
    void givenPointRuleModifyRequest_whenModifyPointRuleResponse_thenReturnPointRuleModifyResponse() throws Exception {
        PointRuleModifyRequest request = new PointRuleModifyRequest();
        ReflectionTestUtils.setField(request, "pointRuleName", "test123");
        ReflectionTestUtils.setField(request, "rate", 10);
        ReflectionTestUtils.setField(request, "cost", null);
        PointRuleModifyResponse response = new PointRuleModifyResponse("test123", 10, null);

        when(pointRuleService.modifyPointRuleResponse(any(PointRuleModifyRequest.class), any())).thenReturn(response);

        mockMvc.perform(put("/api/point-rules/{id}", 1)
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pointRuleName").value("test123"))
                .andExpect(jsonPath("$.rate").value(10))
                .andDo(document("point-rule-modify",
                        pathParameters(
                                parameterWithName("id").description("수정할 포인트 규정 아이디")
                        ),
                        requestFields(
                                fieldWithPath("pointRuleName").description("포인트 규정 명"),
                                fieldWithPath("rate").description("포인트 적립률"),
                                fieldWithPath("cost").description("포인트 적립액")
                        ),
                        responseFields(
                                fieldWithPath("pointRuleName").description("포인트 규정 명"),
                                fieldWithPath("rate").description("포인트 적립률"),
                                fieldWithPath("cost").description("포인트 적립액")
                        )));

    }

    @Test
    @DisplayName("포인트 규정 수정 시 유효성 검사")
    void givenPointRuleModifyRequest_whenValidateRequest_thenReturnIsBadRequest() throws Exception {
        PointRuleModifyRequest request = new PointRuleModifyRequest();
        ReflectionTestUtils.setField(request, "pointRuleName", "");
        ReflectionTestUtils.setField(request, "rate", 10);
        ReflectionTestUtils.setField(request, "cost", null);
        mockMvc.perform(put("/api/point-rules/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("point-rule-modify-validation",
                        pathParameters(
                                parameterWithName("id").description("수정할 포인트 규정 아이디")
                        ),
                        requestFields(
                                fieldWithPath("pointRuleName").description("포인트 규정 명"),
                                fieldWithPath("rate").description("포인트 적립률"),
                                fieldWithPath("cost").description("포인트 적립액")
                        )));
        verify(pointRuleService, never()).modifyPointRuleResponse(any(PointRuleModifyRequest.class), any());
    }

    @Test
    @DisplayName("포인트 규정 삭제")
    void givenId_whenDeletePointRuleResponse_thenReturnNoContent() throws Exception {
        doNothing().when(pointRuleService).deletePointRuleResponse(any());
        mockMvc.perform(delete("/api/point-rules/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("point-rule-delete",
                        pathParameters(
                                parameterWithName("id").description("삭제할 포인트 규정 아이디")
                        )));
        verify(pointRuleService, times(1)).deletePointRuleResponse(any());

    }
}