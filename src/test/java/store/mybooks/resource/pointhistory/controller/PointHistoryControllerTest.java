package store.mybooks.resource.pointhistory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.pointhistory.dto.request.PointHistoryCreateRequest;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryCreateResponse;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryResponse;
import store.mybooks.resource.pointhistory.dto.response.PointResponse;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.pointrule.exception.PointRuleNotExistException;
import store.mybooks.resource.pointrulename.exception.PointRuleNameNotExistException;
import store.mybooks.resource.user.exception.UserNotExistException;

/**
 * packageName    : store.mybooks.resource.pointhistory.controller
 * fileName       : PointHistoryControllerTest
 * author         : damho-lee
 * date           : 3/24/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/24/24          damho-lee          최초 생성
 */
@WebMvcTest(value = PointHistoryController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class PointHistoryControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PointHistoryService pointHistoryService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris(), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("getRemainPoint 테스트")
    void givenUserId_whenGetRemainPoint_thenReturnPointResponse() throws Exception {
        PointResponse expect = new PointResponse(1000);
        when(pointHistoryService.getRemainingPoint(anyLong())).thenReturn(expect);

        mockMvc.perform(get("/api/point-histories/points")
                        .header("X-USER-ID", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remainingPoint").value(expect.getRemainingPoint()))
                .andDo(document("point_history-getRemainPoint",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        responseFields(
                                fieldWithPath("remainingPoint").description("보유 포인트")
                        )));

        verify(pointHistoryService, times(1)).getRemainingPoint(1L);
    }

    @Test
    @DisplayName("getRemainPoint 테스트 - 없는 유저인 경우")
    void givenNotExistsId_whenGetRemainPoint_thenThrowNotFound() throws Exception {
        when(pointHistoryService.getRemainingPoint(anyLong())).thenThrow(new UserNotExistException(1L));

        mockMvc.perform(get("/api/point-histories/points")
                        .header("X-USER-ID", 1L))
                .andExpect(status().isNotFound())
                .andDo(document("point_history-getRemainPoint-notExistsUserId",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        )));

        verify(pointHistoryService, times(1)).getRemainingPoint(1L);
    }

    @Test
    @DisplayName("getPointHistory 테스트")
    void givenPageableAndUserId_whenGetPointHistory_thenReturnPageOfPointHistoryResponse() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        PointHistoryResponse signup = new PointHistoryResponse("회원가입 적립", 1000, LocalDate.of(2024, 3, 1));
        PointHistoryResponse login = new PointHistoryResponse("로그인 적립", 200, LocalDate.of(2024, 3, 1));
        List<PointHistoryResponse> expectList = List.of(signup, login);
        long total = 120;
        Page<PointHistoryResponse> expect = new PageImpl<>(expectList, pageable, total);

        when(pointHistoryService.getPointHistory(any(), anyLong())).thenReturn(expect);

        mockMvc.perform(get("/api/point-histories/history?page=" + pageable.getPageNumber()
                        + "&size=" + pageable.getPageSize())
                        .header("X-USER-ID", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(expectList.size()))
                .andExpect(jsonPath("$.content[0].pointRuleName").value(signup.getPointRuleName()))
                .andExpect(jsonPath("$.content[0].statusCost").value(signup.getStatusCost()))
                .andExpect(jsonPath("$.content[0].createdDate").value(signup.getCreatedDate().toString()))
                .andExpect(jsonPath("$.content[1].pointRuleName").value(login.getPointRuleName()))
                .andExpect(jsonPath("$.content[1].statusCost").value(login.getStatusCost()))
                .andExpect(jsonPath("$.content[1].createdDate").value(login.getCreatedDate().toString()))
                .andDo(document("point_history-getPointHistory",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestParameters(
                                parameterWithName("page").description("요청 페이지 번호(0부터 시작, default = 0)"),
                                parameterWithName("size").description("페이지 사이즈(default = 10)")
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].pointRuleName").description("사유"),
                                fieldWithPath("content[].statusCost").description("적립/사용 포인트"),
                                fieldWithPath("content[].createdDate").description("적립/사용일"),
                                fieldWithPath("pageable").description("페이지정보"),
                                fieldWithPath("pageable.sort").description("페이지 정렬 정보"),
                                fieldWithPath("pageable.sort.sorted").description("페이지 정렬되었는지 여부(true: 정렬 됨)"),
                                fieldWithPath("pageable.sort.unsorted").description("페이지 정렬되지 않았는지 여부(true: 정렬 안 됨)"),
                                fieldWithPath("pageable.sort.empty").description("페이지 정렬 정보가 비어 있는지 여부(true: 비어있음)"),
                                fieldWithPath("pageable.pageSize").description("전체 페이지 수"),
                                fieldWithPath("pageable.pageNumber").description("현재 페이지 번호(0부터 시작)"),
                                fieldWithPath("pageable.offset").description("현재 페이지의 시작 오프셋(0부터 시작)"),
                                fieldWithPath("pageable.paged").description("페이지네이션을 사용하는지 여부(true: 사용함)"),
                                fieldWithPath("pageable.unpaged").description("페이지네이션을 사용하는지 여부(true: 사용 안 함)"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 요소(항목) 수"),
                                fieldWithPath("last").description("마지막 페이지 여부(true: 마지막 페이지)"),
                                fieldWithPath("numberOfElements").description("혀재 페이지의 요소(항목) 수"),
                                fieldWithPath("size").description("페이지 당 요소(항목) 수"),
                                fieldWithPath("sort").description("결과 정렬 정보를 담은 객체"),
                                fieldWithPath("sort.sorted").description("결과가 정렬되었는지 여부(true: 정렬 됨)"),
                                fieldWithPath("sort.unsorted").description("결과가 정렬되지 않았는지 여부(true: 정렬 안 됨)"),
                                fieldWithPath("sort.empty").description("결과 정렬 정보가 비어 있는지 여부(true: 비어있음)"),
                                fieldWithPath("number").description("현재 페이지 번호(0부터 시작)"),
                                fieldWithPath("first").description("첫 페이지 여부(true: 첫 페이지)"),
                                fieldWithPath("empty").description("결과가 비어 있는지 여부(true: 비어있음)")
                        )));

        verify(pointHistoryService, times(1)).getPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("getPointHistory 테스트 - 없는 회원인 경우")
    void givenPageableAndNotExistsUserId_whenGetPointHistory_thenReturnNotFound() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);

        when(pointHistoryService.getPointHistory(any(), anyLong())).thenThrow(new UserNotExistException(1L));

        mockMvc.perform(get("/api/point-histories/history?page=" + pageable.getPageNumber()
                        + "&size=" + pageable.getPageSize())
                        .header("X-USER-ID", 1L))
                .andExpect(status().isNotFound())
                .andDo(document("point_history-getPointHistory-notExistsUserId",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestParameters(
                                parameterWithName("page").description("요청 페이지 번호(0부터 시작, default = 0)"),
                                parameterWithName("size").description("페이지 사이즈(default = 10)")
                        )));
        verify(pointHistoryService, times(1)).getPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("createPointHistory")
    void givenPointHistoryCreateRequestAndUserId_whenCreatePointHistory_thenReturnPointHistoryCreateResponse()
            throws Exception {
        PointHistoryCreateRequest request = new PointHistoryCreateRequest("awer9ga79er12", "로그인 적립", 200);
        PointHistoryCreateResponse response = new PointHistoryCreateResponse(1L);
        when(pointHistoryService.createPointHistory(any(), anyLong())).thenReturn(response);

        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/point-histories")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andDo(document("point_history-createPointHistory",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문번호"),
                                fieldWithPath("pointName").description("적립/사용 사유"),
                                fieldWithPath("pointCost").description("적립/사용 포인트")
                        ),
                        responseFields(
                                fieldWithPath("pointId").description("만들어진 pointHistory 의 ID")
                        )));

        verify(pointHistoryService, times(1)).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("createPointHistory - 없는 회원인 경우")
    void givenPointHistoryCreateRequestAndNotExistsUserId_whenCreatePointHistory_thenReturnPointHistoryCreateResponse()
            throws Exception {
        PointHistoryCreateRequest request = new PointHistoryCreateRequest("awer9ga79er12", "로그인 적립", 200);
        when(pointHistoryService.createPointHistory(any(), anyLong())).thenThrow(new UserNotExistException(1L));

        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/point-histories")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(document("point_history-createPointHistory-notExistsUserId",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문번호"),
                                fieldWithPath("pointName").description("적립/사용 사유"),
                                fieldWithPath("pointCost").description("적립/사용 포인트")
                        )))
                .andDo(print());

        verify(pointHistoryService, times(1)).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("createPointHistory - 없는 포인트 적립/사용 사유인 경우")
    void givenNotExistsPointNameAndUserId_whenCreatePointHistory_thenReturnPointHistoryCreateResponse()
            throws Exception {
        PointHistoryCreateRequest request = new PointHistoryCreateRequest("awer9ga79er12", "로그인 적립", 200);
        when(pointHistoryService.createPointHistory(any(), anyLong())).thenThrow(new PointRuleNameNotExistException());

        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/point-histories")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(document("point_history-createPointHistory-notExistsPointRuleName",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문번호"),
                                fieldWithPath("pointName").description("적립/사용 사유"),
                                fieldWithPath("pointCost").description("적립/사용 포인트")
                        )));

        verify(pointHistoryService, times(1)).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("createPointHistory - 없는 포인트 규정인 경우")
    void givenNotExistsPointRuleAndUserId_whenCreatePointHistory_thenReturnPointHistoryCreateResponse()
            throws Exception {
        PointHistoryCreateRequest request = new PointHistoryCreateRequest("awer9ga79er12", "로그인 적립", 200);
        when(pointHistoryService.createPointHistory(any(), anyLong())).thenThrow(new PointRuleNotExistException());

        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/point-histories")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(document("point_history-createPointHistory-notExistsPointRule",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문번호"),
                                fieldWithPath("pointName").description("적립/사용 사유"),
                                fieldWithPath("pointCost").description("적립/사용 포인트")
                        )));

        verify(pointHistoryService, times(1)).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("createPointHistory - 없는 주문 번호인 경우")
    void givenNotExistsOrderNumberAndUserId_whenCreatePointHistory_thenReturnPointHistoryCreateResponse()
            throws Exception {
        PointHistoryCreateRequest request = new PointHistoryCreateRequest("awer9ga79er12", "로그인 적립", 200);
        when(pointHistoryService.createPointHistory(any(), anyLong())).thenThrow(new BookOrderNotExistException());

        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/point-histories")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(document("point_history-createPointHistory-notExistsOrderNumber",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("orderNumber").description("주문번호"),
                                fieldWithPath("pointName").description("적립/사용 사유"),
                                fieldWithPath("pointCost").description("적립/사용 포인트")
                        )));

        verify(pointHistoryService, times(1)).createPointHistory(any(), anyLong());
    }
}