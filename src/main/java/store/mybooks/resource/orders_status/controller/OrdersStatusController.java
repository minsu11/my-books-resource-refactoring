package store.mybooks.resource.orders_status.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusCreateRequest;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusCreateResponse;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;
import store.mybooks.resource.orders_status.service.OrdersStatusService;

/**
 * packageName    : store.mybooks.resource.orders_status.controller
 * fileName       : OrdersStatusController
 * author         : minsu11
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        minsu11       최초 생성
 */
@RestController
@RequestMapping("/api/orders-statuses")
@RequiredArgsConstructor
public class OrdersStatusController {
    private final OrdersStatusService ordersStatusService;

    /**
     * methodName : getOrdersStatus<br>
     * author : minsu11<br>
     * description : 요청 들어온 id 값에 따른 주문 상태 응답<br>
     *
     * @param ordersStatusId 요청 들어오는 주문 상태 아이디<br>
     * @return 주문 상태 응답 타입에 대한 response entity 반환<br>
     */
    @GetMapping("/{ordersStatusId}")
    public ResponseEntity<OrdersStatusResponse> getOrdersStatus(
            @PathVariable String ordersStatusId
    ) {
        OrdersStatusResponse response = ordersStatusService.getOrdersStatusById(ordersStatusId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * methodName : getOrdersStatusList<br>
     * author : minsu11<br>
     * description : 요청에 따라 주문 상태의 모든 데이터를 리스트로 반환
     *
     * @return 주문 상태 응답 리스트 타입에 대한 response entity 반환<br>
     */
    @GetMapping
    public ResponseEntity<List<OrdersStatusResponse>> getOrdersStatusList(
    ) {
        List<OrdersStatusResponse> ordersStatusResponseList = ordersStatusService.getOrdersStatusList();
        return new ResponseEntity<>(ordersStatusResponseList, HttpStatus.OK);
    }

    /**
     * methodName : createOrdersStatus<br>
     * author : minsu11<br>
     * description : post 요청으로 들어오는 데이터를 저장<br>
     *
     * @param request 등록을 요청한 데이터<br>
     * @return 등록한 주문 상태 타입에 대한 response entity으로 반환<br>
     */
    @PostMapping
    public ResponseEntity<OrdersStatusCreateResponse> createOrdersStatus(
            @RequestBody OrdersStatusCreateRequest request
    ) {
        OrdersStatusCreateResponse response = ordersStatusService.createOrdersStatus(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
