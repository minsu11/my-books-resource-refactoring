package store.mybooks.resource.ordersstatus.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.ordersstatus.dto.mapper.OrdersStatusMapper;
import store.mybooks.resource.ordersstatus.dto.response.OrdersStatusResponse;
import store.mybooks.resource.ordersstatus.entity.OrdersStatus;
import store.mybooks.resource.ordersstatus.exception.OrdersStatusNotExistException;
import store.mybooks.resource.ordersstatus.repository.OrdersStatusRepository;

/**
 * packageName    : store.mybooks.resource.orders_status.service<br>
 * fileName       : OrdersStatusServiceTest<br>
 * author         : minsu11<br>
 * date           : 2/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/20/24        minsu11       최초 생성<br>
 */
@ExtendWith(MockitoExtension.class)
class OrdersStatusServiceUnitTest {
    @InjectMocks
    OrdersStatusService ordersStatusService;

    @Mock
    OrdersStatusRepository ordersStatusRepository;
    @Mock
    OrdersStatusMapper mapper;

    @Test
    @Order(1)
    @DisplayName("요청한 id의 값 데이터가 반환이 되는 지 테스트")
    void givenOrdersStatusId_whenCallFindById_thenReturnOrderStatusResponse() {
        //given
        String ordersStatusId = "test";
        when(ordersStatusRepository.findById(any(String.class))).thenReturn(Optional.of(new OrdersStatus("test")));
        when(mapper.mapToResponse(any(OrdersStatus.class))).thenReturn(new OrdersStatusResponse(ordersStatusId));

        //when
        OrdersStatusResponse expected = new OrdersStatusResponse(ordersStatusId);

        OrdersStatusResponse result = ordersStatusService.getOrdersStatusById(ordersStatusId);

        //then
        Assertions.assertEquals(expected.getId(), result.getId());
        verify(ordersStatusRepository, times(1)).findById(anyString());
        verify(mapper, times(1)).mapToResponse(any());
    }

    @Test
    @Order(2)
    @DisplayName("요청한 id의 값이 데이터 베이스에 없을 경우")
    void givenOrderStatus_whenCallFind_thenReturnOrdersStatus() {
        String orderStatusId = "test";
        when(ordersStatusRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(OrdersStatusNotExistException.class,
                () -> ordersStatusService.getOrdersStatusById(orderStatusId));
        verify(ordersStatusRepository, times(1)).findById(anyString());
    }

    @Test
    @Order(3)
    @DisplayName("api 요청이 들어오면 모든 주문 상태의 데이터를 리스트로 반환하는 테스트")
    void givenOrdersStatusId_whenCallGetOrdersStatusList_thenReturnOrdersStatusResponseList() {
        List<OrdersStatusResponse> expected = List.of(new OrdersStatusResponse("test"));
        when(ordersStatusRepository.getOrdersStatusList()).thenReturn(expected);

        List<OrdersStatusResponse> result = ordersStatusService.getOrdersStatusList();

        Assertions.assertEquals(expected.get(0).getId(), result.get(0).getId());
        verify(ordersStatusRepository, times(1)).getOrdersStatusList();
    }

    @Test
    @Order(4)
    @DisplayName("api 요청이 들어왔지만 DB에 저장된 데이터가 없어서 빈 리스트를 반환하는 테스트")
    void givenOrdersStatusId_whenCallGetOrdersStatusList_thenReturnEmptyList() {
        when(ordersStatusRepository.getOrdersStatusList()).thenReturn(Collections.emptyList());

        List<OrdersStatusResponse> actual = ordersStatusService.getOrdersStatusList();

        Assertions.assertEquals(Collections.emptyList().size(), actual.size());
        verify(ordersStatusRepository, times(1)).getOrdersStatusList();
    }

}