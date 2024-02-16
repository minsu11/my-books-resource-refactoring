package store.mybooks.resource.orders_status.service;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusRequest;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;
import store.mybooks.resource.orders_status.entity.OrdersStatus;
import store.mybooks.resource.orders_status.exception.OrdersStatusNotFoundException;
import store.mybooks.resource.orders_status.repository.OrdersStatusRepository;

/**
 * packageName    : store.mybooks.resource.orders_status.service
 * fileName       : OrdersStatusService
 * author         : minsu11
 * date           : 2/15/24
 * description    : orders_status 테이블의 등록, 수정, 삭제 메서드를 담은 service
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
@Service
@AllArgsConstructor
public class OrdersStatusService {
    private OrdersStatusRepository ordersStatusRepository;

    @Transactional(readOnly = true)
    public OrdersStatusResponse getOrdersStatusById(String ordersStatusId) {

        OrdersStatus ordersStatus = ordersStatusRepository.findById(
                ordersStatusId).orElseThrow(() -> new OrdersStatusNotFoundException());
        return ordersStatus.convertToOrdersStatusResponse();
    }

    @Transactional(readOnly = true)
    public List<OrdersStatusResponse> getOrdersStatusList() {
        List<OrdersStatusResponse> ordersStatusResponses = ordersStatusRepository.getOrdersStatusList();

        if (Objects.isNull(ordersStatusResponses)) {
            throw new NullPointerException("order status null");
        }
        return ordersStatusResponses;
    }

    @Transactional
    public OrdersStatusResponse createOrdersStatus(OrdersStatusRequest request) {
        OrdersStatus ordersStatus = ordersStatusRepository.findById(request.getId())
                .orElseThrow(OrdersStatusNotFoundException::new);
        ordersStatusRepository.save(ordersStatus);
        return ordersStatus.convertToOrdersStatusResponse();
    }


}
