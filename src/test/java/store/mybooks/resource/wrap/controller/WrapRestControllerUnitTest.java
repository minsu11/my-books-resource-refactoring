package store.mybooks.resource.wrap.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
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
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = WrapRestController.class)
class WrapRestControllerUnitTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WrapService wrapService;


    @Test
    @DisplayName("id에 맞는 포장지 조회 성공 테스트")
    void givenIntegerId_whenGetWrapById_thenReturnWrapResponse() throws Exception {
        WrapResponse wrapResponse = new WrapResponse("test", 1000, true);
        when(wrapService.getWrapById(any())).thenReturn(wrapResponse);

        mockMvc.perform(get("/api/wraps/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(wrapResponse.getName()))
                .andExpect(jsonPath("$.cost").value(wrapResponse.getCost()))
                .andExpect(jsonPath("$.isAvailable").value(wrapResponse.getIsAvailable()));
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

        mockMvc.perform(get("/api/wraps/page"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value(wrapPageResponses.get(0).getName()))
                .andExpect(jsonPath("$.content[0].cost").value(wrapPageResponses.get(0).getCost()))
                .andExpect(jsonPath("$.content[0].isAvailable").value(wrapPageResponses.get(0).getIsAvailable()))
                .andExpect(jsonPath("$.content[1].name").value(wrapPageResponses.get(1).getName()))
                .andExpect(jsonPath("$.content[1].cost").value(wrapPageResponses.get(1).getCost()))
                .andExpect(jsonPath("$.content[1].isAvailable").value(wrapPageResponses.get(1).getIsAvailable()));
        verify(wrapService, times(1)).getWrapPage(any());
    }

    @Test
    @DisplayName("전체 포장지 목록 조회")
    void given_whenGetWrapResponseList_thenReturnGetWrapResponseList() throws Exception {
        List<WrapResponse> wrapResponses = List.of(new WrapResponse("test", 100, true));
        when(wrapService.getWrapResponseList()).thenReturn(wrapResponses);

        mockMvc.perform(get("/api/wraps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(wrapResponses.get(0).getName()))
                .andExpect(jsonPath("$[0].cost").value(wrapResponses.get(0).getCost()))
                .andExpect(jsonPath("$[0].isAvailable").value(wrapResponses.get(0).getIsAvailable()));
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
                .andExpect(jsonPath("$.cost").value(wrapCreateResponse.getCost()));

        verify(wrapService, times(1)).createWrap(any());
    }


    @Test
    @DisplayName("포장지 등록 실패 테스트(유효성 테스트 실패: 이릅 빈 값 )")
    void givenWrapCreateRequest_whenCreateWrap_thenThrowWrapValidationFailedException() throws Exception {
        WrapCreateRequest wrapCreateRequest = new WrapCreateRequest();
        ReflectionTestUtils.setField(wrapCreateRequest, "name", "");
        ReflectionTestUtils.setField(wrapCreateRequest, "cost", 100);
        mockMvc.perform(post("/api/wraps")
                        .content(objectMapper.writeValueAsString(wrapCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
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
                .andDo(print());
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
                .andDo(print());
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
                .andDo(print());
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
                .andExpect(jsonPath("$.cost").value(modifyResponse.getCost()));
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
                .andDo(print());
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
                .andDo(print());

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
                .andDo(print());

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
                .andDo(print());
        verify(wrapService, never()).modifyWrap(any(), any());
    }


    @Test
    @DisplayName("포장지 삭제 성공 테스트")
    void givenIntegerId_whenDeleteWrap_thenHttpStatusCode() throws Exception {
        doNothing().when(wrapService).deleteWrap(1);

        mockMvc.perform(delete("/api/wraps/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
        verify(wrapService, times(1)).deleteWrap(any());

    }
}