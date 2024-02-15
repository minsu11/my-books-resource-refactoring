package store.mybooks.resource.order_detail_status.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.order_detail_status.dto.response.OrderDetailStatusResponse;
import store.mybooks.resource.order_detail_status.entity.OrderDetailStatus;
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
@AllArgsConstructor
public class OrderDetailStatusService {
    private OrderDetailStatusRepository orderDetailStatusRepository;

    @Transactional(readOnly = true)
    public OrderDetailStatusResponse getOrderDetailStatus(String id) {
        OrderDetailStatus orderDetailStatus = orderDetailStatusRepository.findById(id).orElseThrow(() -> new OrderDetailStatusNotFoundException("order detail이 없음"));
        return orderDetailStatus.convertToOrderDetailStatusResponse();
    }

    @Transactional(readOnly = true)
    public List<OrderDetailStatusResponse> getOrderDetailStatusList() {
        return orderDetailStatusRepository.getOrderDetailStatusResponseList();
    }


}
