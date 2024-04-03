package store.mybooks.resource.wrap.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
import store.mybooks.resource.wrap.dto.request.WrapCreateRequest;
import store.mybooks.resource.wrap.dto.request.WrapModifyRequest;
import store.mybooks.resource.wrap.dto.response.WrapCreateResponse;
import store.mybooks.resource.wrap.dto.response.WrapModifyResponse;
import store.mybooks.resource.wrap.dto.response.WrapPageResponse;
import store.mybooks.resource.wrap.dto.response.WrapResponse;
import store.mybooks.resource.wrap.service.WrapService;

/**
 * packageName    : store.mybooks.resource.wrap.controller<br>
 * fileName       : WrapRestControllerUnitTest<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(value = WrapRestController.class)
class WrapRestControllerUnitTest {

    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WrapService wrapService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("id에 맞는 포장지 조회 성공 테스트")
    void givenIntegerId_whenGetWrapById_thenReturnWrapResponse() throws Exception {
        WrapResponse wrapResponse = new WrapResponse(1, "test", 1000, true);
        when(wrapService.getWrapById(any())).thenReturn(wrapResponse);

        mockMvc.perform(get("/api/wraps/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(wrapResponse.getName()))
                .andExpect(jsonPath("$.cost").value(wrapResponse.getCost()))
                .andExpect(jsonPath("$.isAvailable").value(wrapResponse.getIsAvailable()))
                .andDo(document("wrap-find-success",
                        pathParameters(
                                parameterWithName("id").description("조회할 포장지 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포장지 아이디"),
                                fieldWithPath("name").description("포장지 이름"),
                                fieldWithPath("cost").description("포장지 가격"),
                                fieldWithPath("isAvailable").description("포장지 사용 가능 여부")
                        )));
        verify(wrapService, times(1)).getWrapById(any());
    }


    @Test
    @DisplayName("pagination 포장지 목록 조회 성공 테스트")
    void givenPageable_whenGetWrapPage_thenReturnWrapPageResponsePage() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        List<WrapPageResponse> wrapPageResponses = List.of(
                new WrapPageResponse(1, "test", 1000, true),
                new WrapPageResponse(2, "test2", 100, true));
        Page<WrapPageResponse> wrapPageResponsePage = new PageImpl<>(wrapPageResponses, pageable, 2);
        when(wrapService.getWrapPage(any())).thenReturn(wrapPageResponsePage);

        mockMvc.perform(get("/api/wraps/page?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value(wrapPageResponses.get(0).getName()))
                .andExpect(jsonPath("$.content[0].cost").value(wrapPageResponses.get(0).getCost()))
                .andExpect(jsonPath("$.content[0].isAvailable").value(wrapPageResponses.get(0).getIsAvailable()))
                .andExpect(jsonPath("$.content[1].name").value(wrapPageResponses.get(1).getName()))
                .andExpect(jsonPath("$.content[1].cost").value(wrapPageResponses.get(1).getCost()))
                .andExpect(jsonPath("$.content[1].isAvailable").value(wrapPageResponses.get(1).getIsAvailable()))
                .andDo(document("wrap-page-find-success",
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content[].id").description("포장지 아이디"),
                                fieldWithPath("content[].name").description("포장지 이름"),
                                fieldWithPath("content[].isAvailable").description("포장지 사용 가능 여부"),
                                fieldWithPath("content[].cost").description("포장지 가격"),
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
                        )
                ));
        verify(wrapService, times(1)).getWrapPage(any());
    }

    @Test
    @DisplayName("전체 포장지 목록 조회")
    void given_whenGetWrapResponseList_thenReturnGetWrapResponseList() throws Exception {
        List<WrapResponse> wrapResponses = List.of(new WrapResponse(1, "test", 100, true));
        when(wrapService.getWrapResponseList()).thenReturn(wrapResponses);

        mockMvc.perform(get("/api/wraps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(wrapResponses.get(0).getName()))
                .andExpect(jsonPath("$[0].cost").value(wrapResponses.get(0).getCost()))
                .andExpect(jsonPath("$[0].isAvailable").value(wrapResponses.get(0).getIsAvailable()))
                .andDo(document("wrap-list-find-success",
                        responseFields(
                                fieldWithPath("[].id").description("포장지 아이디"),
                                fieldWithPath("[].name").description("포장지 이름"),
                                fieldWithPath("[].cost").description("포장지 가격"),
                                fieldWithPath("[].isAvailable").description("포장지 사용 여부")

                        )));
        verify(wrapService, times(1)).getWrapResponseList();
    }

    @Test
    @DisplayName("포장지 등록 성공 테스트(유효성 테스트 통과)")
    void givenWrapCreateRequest_whenCreateWrap_thenReturnWrapCreateResponse() throws Exception {
        WrapCreateRequest request = new WrapCreateRequest();
        ReflectionTestUtils.setField(request, "name", "test1");
        ReflectionTestUtils.setField(request, "cost", 100);
        WrapCreateResponse wrapCreateResponse = new WrapCreateResponse("test1", 100);
        when(wrapService.createWrap(any(WrapCreateRequest.class))).thenReturn(wrapCreateResponse);

        mockMvc.perform(post("/api/wraps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(wrapCreateResponse.getName()))
                .andExpect(jsonPath("$.cost").value(wrapCreateResponse.getCost()))
                .andDo(document("wrap-create-success",
                        requestFields(
                                fieldWithPath("name").description("등록할 포장지 이름"),
                                fieldWithPath("cost").description("등록할 포장지 가격")
                        ),
                        responseFields(
                                fieldWithPath("name").description("등록한 포장지"),
                                fieldWithPath("cost").description("등록한 포장지 가격")
                        )));

        verify(wrapService, times(1)).createWrap(any());
    }


    @Test
    @DisplayName("포장지 등록 실패 테스트(유효성 테스트 실패: 이릅 빈 값 )")
    void givenWrapCreateRequest_whenCreateWrap_thenThrowWrapValidationFailedException() throws Exception {
        WrapCreateRequest request = new WrapCreateRequest();
        ReflectionTestUtils.setField(request, "name", "");
        ReflectionTestUtils.setField(request, "cost", 100);
        mockMvc.perform(post("/api/wraps")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("wrap-create-name-empty-validation",
                        requestFields(
                                fieldWithPath("name").description("등록할 포장지 이름"),
                                fieldWithPath("cost").description("등록할 포장지 가격")
                        )
                ));
        verify(wrapService, never()).createWrap(any());
    }

    @Test
    @DisplayName("포장지 등록 실패 테스트(유효성 테스트 실패: 글자 수 최대 넘었을 때 )")
    void givenWrapCreateRequest_whenCreateWrap_thenThrowWrapValidationFailedException2() throws Exception {
        WrapCreateRequest wrapCreateRequest = new WrapCreateRequest();
        ReflectionTestUtils.setField(wrapCreateRequest, "name", "abcdfwqweasdasasfasfasadasdasdasdaasfasdfasdfasdfasdasdasd");
        ReflectionTestUtils.setField(wrapCreateRequest, "cost", 100);
        mockMvc.perform(post("/api/wraps")
                        .content(objectMapper.writeValueAsString(wrapCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("wrap-create-name-max-size-validation",
                        requestFields(
                                fieldWithPath("name").description("등록할 포장지 이름"),
                                fieldWithPath("cost").description("등록할 포장지 가격")
                        )));
        verify(wrapService, never()).createWrap(any());
    }

    @Test
    @DisplayName("포장지 등록 실패 테스트(유효성 테스트 실패: cost 음수)")
    void givenWrapCreateRequest_whenCreateWrap_thenThrowWrapValidationFailedException3() throws Exception {
        WrapCreateRequest wrapCreateRequest = new WrapCreateRequest();
        ReflectionTestUtils.setField(wrapCreateRequest, "name", "test");
        ReflectionTestUtils.setField(wrapCreateRequest, "cost", -1);
        mockMvc.perform(post("/api/wraps")
                        .content(objectMapper.writeValueAsString(wrapCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("wrap-create-cost-negative-validation",
                        requestFields(
                                fieldWithPath("name").description("등록할 포장지"),
                                fieldWithPath("cost").description("등록할 포장지 가격")
                        )));
        verify(wrapService, never()).createWrap(any());
    }

    @Test
    @DisplayName("포장지 등록 실패 테스트(유효성 테스트 실패: cost 최댓값 넘을 떄 )")
    void givenWrapCreateRequest_whenCreateWrap_thenThrowWrapValidationFailedException4() throws Exception {
        WrapCreateRequest wrapCreateRequest = new WrapCreateRequest();
        ReflectionTestUtils.setField(wrapCreateRequest, "name", "test");
        ReflectionTestUtils.setField(wrapCreateRequest, "cost", 100001);
        mockMvc.perform(post("/api/wraps")
                        .content(objectMapper.writeValueAsString(wrapCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("wrap-cost-max-value-validation",
                        requestFields(
                                fieldWithPath("name").description("등록할 포장지 이름"),
                                fieldWithPath("cost").description("등록할 포장지 가격")
                        )));

        verify(wrapService, never()).createWrap(any());
    }


    @Test
    @DisplayName("포장지 수정 성공 테스트(유효성 성공 테스트)")
    void givenWrapModifyRequest_whenModifyWrap_thenReturnWrapModifyResponse() throws Exception {
        WrapModifyRequest request = new WrapModifyRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "cost", 1000);
        WrapModifyResponse modifyResponse = new WrapModifyResponse("test", 1000);
        when(wrapService.modifyWrap(any(WrapModifyRequest.class), any())).thenReturn(modifyResponse);

        mockMvc.perform(put("/api/wraps/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(modifyResponse.getName()))
                .andExpect(jsonPath("$.cost").value(modifyResponse.getCost()))
                .andDo(document("wrap-modify-success",
                        pathParameters(
                                parameterWithName("id").description("수정할 포장지 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 포장지 이름"),
                                fieldWithPath("cost").description("수정할 포장지 가격")
                        ),
                        responseFields(
                                fieldWithPath("name").description("수정한 포장지 이름"),
                                fieldWithPath("cost").description("수정한 포장지 가격")
                        )));
        verify(wrapService, times(1)).modifyWrap(any(), any());
    }

    @Test
    @DisplayName("포장지 수정 실패 테스트(유효성 실패 테스트(이름이 빈 값)")
    void givenWrapModifyRequest_whenModifyWrap_thenThrowWrapValidationFailedException() throws Exception {
        WrapModifyRequest modifyRequest = new WrapModifyRequest();
        ReflectionTestUtils.setField(modifyRequest, "name", "");
        ReflectionTestUtils.setField(modifyRequest, "cost", 1000);
        mockMvc.perform(put("/api/wraps/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("wrap-modify-name-empty-validation",
                        pathParameters(
                                parameterWithName("id").description("수정할 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 포장지 이름"),
                                fieldWithPath("cost").description("수정할 포장지 가격")
                        )));
        verify(wrapService, never()).modifyWrap(any(), any());
    }

    @Test
    @DisplayName("포장지 수정 실패 테스트(유효성 실패 테스트(이름 글자 수 최대 길이 넘김)")
    void givenWrapModifyRequest_whenModifyWrap_thenThrowWrapValidationFailedException2() throws Exception {
        WrapModifyRequest request = new WrapModifyRequest();
        ReflectionTestUtils.setField(request, "name", "test1test2test3test4t");
        ReflectionTestUtils.setField(request, "cost", 1000);
        mockMvc.perform(put("/api/wraps/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("wrap-modify-name-max-over-size-validation",
                        pathParameters(
                                parameterWithName("id").description("수정할 포장지 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 포장지 이름"),
                                fieldWithPath("cost").description("수정할 포장지 가격")
                        )));

        verify(wrapService, never()).modifyWrap(any(), any());
    }

    @Test
    @DisplayName("포장지 수정 실패 테스트(유효성 실패 테스트(cost 음수)")
    void givenWrapModifyRequest_whenModifyWrap_thenThrowWrapValidationFailedException3() throws Exception {
        WrapModifyRequest request = new WrapModifyRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "cost", -1);
        mockMvc.perform(put("/api/wraps/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("wrap-modify-cost-negative-validation",
                        pathParameters(
                                parameterWithName("id").description("수정할 포장지 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 포장지 이름"),
                                fieldWithPath("cost").description("수정할 포장지 가격")
                        )
                ));

        verify(wrapService, never()).modifyWrap(any(), any());
    }

    @Test
    @DisplayName("포장지 수정 실패 테스트(유효성 실패 테스트(cost가 최댓 값 넘긴 경우)")
    void givenWrapModifyRequest_whenModifyWrap_thenThrowWrapValidationFailedException4() throws Exception {
        WrapModifyRequest request = new WrapModifyRequest();
        ReflectionTestUtils.setField(request, "name", "test");
        ReflectionTestUtils.setField(request, "cost", 100001);
        mockMvc.perform(put("/api/wraps/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("wrap-modify-cost-max-value-validation",
                        pathParameters(
                                parameterWithName("id").description("수정할 포장지 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 포장지 이름"),
                                fieldWithPath("cost").description("수정할 포장지 가격")
                        )
                ));
        verify(wrapService, never()).modifyWrap(any(), any());
    }


    @Test
    @DisplayName("포장지 삭제 성공 테스트")
    void givenIntegerId_whenDeleteWrap_thenHttpStatusCode() throws Exception {
        doNothing().when(wrapService).deleteWrap(1);

        mockMvc.perform(delete("/api/wraps/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("wrap-delete-success",
                        pathParameters(
                                parameterWithName("id").description("삭제할 포장지 아이디")
                        )));
        verify(wrapService, times(1)).deleteWrap(any());

    }
}