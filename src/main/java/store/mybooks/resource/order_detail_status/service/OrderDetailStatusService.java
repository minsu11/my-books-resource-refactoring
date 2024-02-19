package store.mybooks.resource.order_detail_status.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.order_detail_status.dto.request.OrderDetailStatusRequest;
import store.mybooks.resource.order_detail_status.dto.response.OrderDetailMapper;
import store.mybooks.resource.order_detail_status.dto.response.OrderDetailStatusCreateResponse;
import store.mybooks.resource.order_detail_status.dto.response.OrderDetailStatusResponse;
import store.mybooks.resource.order_detail_status.entity.OrderDetailStatus;
import store.mybooks.resource.order_detail_status.exception.OrderDetailStatusAlreadyExistException;
import store.mybooks.resource.order_detail_status.exception.OrderDetailStatusNotFoundException;
import store.mybooks.resource.order_detail_status.repository.OrderDetailStatusRepository;

/**
 * packageName    : store.mybooks.resource.order_detail_status.service <br>
 * fileName       : OrderDetailStatusService <br>
 * author         : minsu11 <br>
 * date           : 2/16/24 <br>
 * description    : 상세 주문 상태 데이터를 등록 및 조회를 할 수 있는 Service <br>
 * =========================================================== <br>
 * DATE              AUTHOR             NOTE <br>
 * ----------------------------------------------------------- <br>
 * 2/16/24        minsu11       최초 생성 <br>
 * 2/16/24        minsu11       최초 구현 <br>
 * 2/18/24        minsu11       데이터 베이스에서 반환된 데이터가 빈 값인 <br>
 * 경우 exception 반환에서 빈 리스트로 변경   <br>
 */
@Service
@RequiredArgsConstructor
public class OrderDetailStatusService {
    private final OrderDetailStatusRepository orderDetailStatusRepository;
    private final OrderDetailMapper mapper;


    /**
     * methodName : getOrderDetailStatus
     * author : minsu11
     * description : id와 동일한 상세 주문 상태 데이터를 가지고 옴
     *
     * @param id
     * @return order detail status response
     * @throws OrderDetailStatusNotFoundException id와 동일한 상세 주문 상태가 없는 경우
     */
    @Transactional(readOnly = true)
    public OrderDetailStatusResponse getOrderDetailStatus(String id) {
        OrderDetailStatus orderDetailStatus = orderDetailStatusRepository.findById(id).orElseThrow(() -> new OrderDetailStatusNotFoundException("order detail이 없음"));
        return mapper.mapToOrderDetailStatusResponse(orderDetailStatus);
    }


    /**
     * methodName : getOrderDetailStatusList
     * author : minsu11
     * description : 모든 상세 주문의 데이터를 가지고 옴.
     * 아무런 값이 없을 떈 빈 리스트를 반환을 해줌
     *
     * @return list
     */
    @Transactional(readOnly = true)
    public List<OrderDetailStatusResponse> getOrderDetailStatusList() {

        return orderDetailStatusRepository.getOrderDetailStatusResponseList();
    }

    /**
     * methodName : createOrderDetailStatus
     * author : minsu11
     * description : 상세 주문 데이터를 생성. 데이터 베이스에 이미 있는 데이터면 throw를 던짐
     *
     * @param request
     * @return order detail status create response
     * @throws OrderDetailStatusAlreadyExistException database 안에 데이터가 있는 경우
     */
    public OrderDetailStatusCreateResponse createOrderDetailStatus(OrderDetailStatusRequest request) {
        if (orderDetailStatusRepository.findById(request.getId()).isPresent()) {
            throw new OrderDetailStatusAlreadyExistException("이미 존재한 상태");
        }
        OrderDetailStatus orderDetailStatus = new OrderDetailStatus(request.getId());
        orderDetailStatusRepository.save(orderDetailStatus);

        return mapper.mapToOrderDetailStatusCreateResponse(orderDetailStatus);
    }

}
