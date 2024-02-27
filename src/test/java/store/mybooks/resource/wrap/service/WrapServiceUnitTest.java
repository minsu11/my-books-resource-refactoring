package store.mybooks.resource.wrap.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.wrap.dto.WrapMapper;
import store.mybooks.resource.wrap.dto.request.WrapCreateRequest;
import store.mybooks.resource.wrap.dto.request.WrapModifyRequest;
import store.mybooks.resource.wrap.dto.response.WrapCreateResponse;
import store.mybooks.resource.wrap.dto.response.WrapModifyResponse;
import store.mybooks.resource.wrap.dto.response.WrapPageResponse;
import store.mybooks.resource.wrap.dto.response.WrapResponse;
import store.mybooks.resource.wrap.entity.Wrap;
import store.mybooks.resource.wrap.exception.WrapAlreadyExistException;
import store.mybooks.resource.wrap.exception.WrapNotExistException;
import store.mybooks.resource.wrap.repository.WrapRepository;

/**
 * packageName    : store.mybooks.resource.wrap.service<br>
 * fileName       : WrapServiceTest<br>
 * author         : minsu11<br>
 * date           : 2/28/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/28/24        minsu11       최초 생성<br>
 */
@ExtendWith(MockitoExtension.class)
class WrapServiceUnitTest {
    @InjectMocks
    WrapService wrapService;

    @Mock
    WrapRepository wrapRepository;

    @Mock
    WrapMapper wrapMapper;

    @Test
    @DisplayName("id에 따른 조회 성공 테스트")
    void givenIntegerId_whenFindWrapResponseId_thenReturnWrapResponse() {
        Integer id = 1;
        WrapResponse expected = new WrapResponse("test", 100, true);
        when(wrapRepository.findWrapResponseById(any())).thenReturn(Optional.of(expected));

        WrapResponse actual = wrapService.getWrapById(id);

        Assertions.assertEquals(expected, actual);

        verify(wrapRepository, times(1)).findWrapResponseById(any());
    }

    @Test
    @DisplayName("id에 따른 조회 실패 테스트")
    void givenIntegerId_whenFindWrapResponseById_thenThrowWrapNotExistException() {
        Integer id = 1;
        when(wrapRepository.findWrapResponseById(any())).thenThrow(WrapNotExistException.class);

        Assertions.assertThrows(WrapNotExistException.class, () -> wrapService.getWrapById(id));
        verify(wrapRepository, times(1)).findWrapResponseById(any());
    }

    @Test
    @DisplayName("사용 중인 모든 포장지 목록 조회 테스트")
    void given_whenGetWrapResponseList_thenReturnWrapResponseList() {
        List<WrapResponse> expected = List.of(new WrapResponse("test", 1000, true));
        when(wrapRepository.getWrapResponseList()).thenReturn(expected);

        List<WrapResponse> actual = wrapService.getWrapResponseList();

        Assertions.assertEquals(expected, actual);

        verify(wrapRepository, times(1)).getWrapResponseList();
    }

    @Test
    @DisplayName("사용 중인 포장지 목록이 없는 경우 테스트 ")
    void given_whenGetWrapResponseList_thenReturnEmptyList() {
        when(wrapRepository.getWrapResponseList()).thenReturn(Collections.emptyList());
        List<WrapResponse> actual = wrapService.getWrapResponseList();

        Assertions.assertEquals(0, actual.size());

        verify(wrapRepository, times(1)).getWrapResponseList();

    }

    @Test
    @DisplayName("사용 중인 포장지 페이징 테스트")
    void getWrapPage() {
        Pageable pageable = PageRequest.of(0, 2);
        List<WrapPageResponse> expectList = List.of(new WrapPageResponse(1, "test", 1000, true),
                new WrapPageResponse(3, "test2", 2000, true));
        Page<WrapPageResponse> expect = new PageImpl<>(expectList, pageable, 5);

        when(wrapRepository.getPageBy(any())).thenReturn(expect);

        Page<WrapPageResponse> actual = wrapService.getWrapPage(pageable);

        Assertions.assertEquals(expect, actual);

        verify(wrapRepository, times(1)).getPageBy(any());

    }

    @Test
    @DisplayName("포장지 등록 성공 테스트")
    void givenWrapCreateRequest_whenSave_thenReturnWrapCreateResponse(@Mock Wrap wrap) {
        WrapCreateRequest request = new WrapCreateRequest("test", 100);
        WrapCreateResponse expected = new WrapCreateResponse("test", 100);

        when(wrapRepository.existWrap(anyString())).thenReturn(false);
        when(wrapRepository.save(any())).thenReturn(wrap);
        when(wrapMapper.mapToWrapCreateResponse(any(Wrap.class))).thenReturn(expected);

        WrapCreateResponse actual = wrapService.createWrap(request);

        Assertions.assertEquals(expected, actual);

        verify(wrapRepository, times(1)).existWrap(anyString());
        verify(wrapRepository, times(1)).save(any());
        verify(wrapMapper, times(1)).mapToWrapCreateResponse(any(Wrap.class));
    }

    @Test
    @DisplayName("포장지 등록 실패 테스트")
    void givenWrapCreateRequest_whenSave_thenThrowWrapAlreadyExistException() {
        WrapCreateRequest request = new WrapCreateRequest("test", 100);
        when(wrapRepository.existWrap(anyString())).thenReturn(true);

        Assertions.assertThrows(WrapAlreadyExistException.class, () -> wrapService.createWrap(request));

        verify(wrapRepository, times(1)).existWrap(anyString());
        verify(wrapRepository, never()).save(any());
        verify(wrapMapper, never()).mapToWrapCreateResponse(any(Wrap.class));
    }


    @Test
    @DisplayName("포장지 수정 성공 테스트")
    void givenWrapModifyReqeustAndIntegerId_whenModifyWrap_thenReturnWrapModifyResponse(@Mock Wrap wrap) {
        WrapModifyRequest request = new WrapModifyRequest("test", 100);
        WrapModifyResponse expected = new WrapModifyResponse("test", 100);
        when(wrapRepository.findById(any())).thenReturn(Optional.of(wrap));
        when(wrapMapper.mapToWrapModifyResponse(any(Wrap.class))).thenReturn(expected);

        WrapModifyResponse actual = wrapService.modifyWrap(request, 1);

        Assertions.assertEquals(expected, actual);
        verify(wrapRepository, times(1)).findById(any());
        verify(wrapMapper, times(1)).mapToWrapModifyResponse(any());

    }

    @Test
    @DisplayName("포장지 수정 실패 테스트")
    void givenWrapModifyReqeustAndIntegerId_whenModifyWrap_thenThrowWrapNotExistException(@Mock Wrap wrap) {
        WrapModifyRequest request = new WrapModifyRequest("test", 100);
        when(wrapRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(WrapNotExistException.class, () -> wrapService.modifyWrap(request, 1));

        verify(wrapRepository, times(1)).findById(any());
        verify(wrapMapper, never()).mapToWrapModifyResponse(any());

    }

    @Test
    @DisplayName("포장지 삭제 성공 테스트")
    void givenIntegerId_whenFindByIdAndModifyIsAvailable(@Mock Wrap wrap) {

        when(wrapRepository.findById(any())).thenReturn(Optional.of(wrap));
        doNothing().when(wrap).modifyIsAvailable(any());

        wrapService.deleteWrap(1);
        Assertions.assertFalse(wrap.getIsAvailable());
        verify(wrapRepository, times(1)).findById(any());
        verify(wrap, times(1)).modifyIsAvailable(any());
    }


    @Test
    @DisplayName("포장지 삭제 실패 테스트")
    void givenIntegerId_whenFindByIdAndModifyIsAvailable_thenThrowWrapNotExistException(@Mock Wrap wrap) {

        when(wrapRepository.findById(any())).thenThrow(WrapNotExistException.class);

        Assertions.assertThrows(WrapNotExistException.class, () -> wrapService.deleteWrap(1));

        verify(wrapRepository, times(1)).findById(any());
        verify(wrap, never()).modifyIsAvailable(any());
    }
}