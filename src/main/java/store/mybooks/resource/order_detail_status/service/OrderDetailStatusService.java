package store.mybooks.resource.order_detail_status.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.order_detail_status.dto.request.OrderDetailStatusRequest;
import store.mybooks.resource.order_detail_status.dto.response.OrderDetailMapper;
import store.mybooks.resource.order_detail_status.dto.response.OrderDetailStatusResponse;
import store.mybooks.resource.order_detail_status.entity.OrderDetailStatus;
import store.mybooks.resource.order_detail_status.exception.OrderDetailStatusAlreadyExistException;
import store.mybooks.resource.order_detail_status.exception.OrderDetailStatusNotFoundException;
import store.mybooks.resource.order_detail_status.repository.OrderDetailStatusRepository;

/**
 * packageName    : store.mybooks.resource.order_detail_status.service
 * fileName       : OrderDetailStatusService
 * author         : minsu11
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        minsu11       최초 생성
 */
@Service
@RequiredArgsConstructor
public class OrderDetailStatusService {
    private final OrderDetailStatusRepository orderDetailStatusRepository;
    private final OrderDetailMapper mapper;

    @Transactional(readOnly = true)
    public OrderDetailStatusResponse getOrderDetailStatus(String id) {
        OrderDetailStatus orderDetailStatus = orderDetailStatusRepository.findById(id).orElseThrow(() -> new OrderDetailStatusNotFoundException("order detail이 없음"));
        return mapper.mapToOrderDetailStatusResponse(orderDetailStatus);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailStatusResponse> getOrderDetailStatusList() {
        return orderDetailStatusRepository.getOrderDetailStatusResponseList();
    }

    public OrderDetailStatusResponse createOrderDetailStatus(OrderDetailStatusRequest request) {
        if (orderDetailStatusRepository.findById(request.getId()).isPresent()) {
            throw new OrderDetailStatusAlreadyExistException("이미 존재한 상태");
        }
        OrderDetailStatus orderDetailStatus = new OrderDetailStatus(request.getId());
        orderDetailStatusRepository.save(orderDetailStatus);

        return orderDetailStatus.convertToOrderDetailStatusResponse();
    }

    public OrderDetailStatusResponse deleteOrderDetailStatus(String id) {
        OrderDetailStatus orderDetailStatus = orderDetailStatusRepository.findById(id)
                .orElseThrow(OrderDetailStatusNotFoundException::new);
        orderDetailStatusRepository.delete(orderDetailStatus);
        return orderDetailStatus.convertToOrderDetailStatusResponse();
    }
}
