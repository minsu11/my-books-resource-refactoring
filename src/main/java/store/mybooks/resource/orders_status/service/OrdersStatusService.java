package store.mybooks.resource.orders_status.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusCreateRequest;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusCreateResponse;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusMapper;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;
import store.mybooks.resource.orders_status.entity.OrdersStatus;
import store.mybooks.resource.orders_status.exception.OrdersStatusAlreadyExistException;
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
@RequiredArgsConstructor
public class OrdersStatusService {
    private final OrdersStatusRepository ordersStatusRepository;
    private final OrdersStatusMapper mapper;

    @Transactional(readOnly = true)
    public OrdersStatusResponse getOrdersStatusById(String ordersStatusId) {

        OrdersStatus ordersStatus = ordersStatusRepository.findById(
                ordersStatusId).orElseThrow(OrdersStatusNotFoundException::new);
        return mapper.mapToResponse(ordersStatus);
    }

    @Transactional(readOnly = true)
    public List<OrdersStatusResponse> getOrdersStatusList() {
        return ordersStatusRepository.getOrdersStatusList();
    }


    @Transactional
    public OrdersStatusCreateResponse createOrdersStatus(OrdersStatusCreateRequest request) {
        if (ordersStatusRepository.findById(request.getId()).isPresent()) {
            throw new OrdersStatusAlreadyExistException("아이디가 이미 존재");
        }
        OrdersStatus ordersStatus = new OrdersStatus(request);
        ordersStatusRepository.save(ordersStatus);
        return mapper.mapToCreateResponse(ordersStatus);
    }


}
