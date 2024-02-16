package store.mybooks.resource.orders_status.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusRequest;
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
@RequestMapping("/api/orders-status")
@AllArgsConstructor
public class OrdersStatusController {
    private OrdersStatusService ordersStatusService;

    @GetMapping("/{id}")
    public ResponseEntity<OrdersStatusResponse> getOrdersStatus(
            @PathVariable String id
    ) {
        OrdersStatusResponse response = ordersStatusService.getOrdersStatusById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrdersStatusResponse>> getOrdersStatusList(
    ) {
        List<OrdersStatusResponse> ordersStatusResponseList = ordersStatusService.getOrdersStatusList();
        return new ResponseEntity<>(ordersStatusResponseList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrdersStatusResponse> createOrdersStatus(
            @RequestBody OrdersStatusRequest request
    ) {
        OrdersStatusResponse response = ordersStatusService.createOrdersStatus(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<OrdersStatusResponse> deleteOrdersStatus(@RequestBody OrdersStatusRequest request) {
        OrdersStatusResponse response = ordersStatusService.deleteOrderStatus(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
