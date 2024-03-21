package store.mybooks.resource.orderdetail.contorller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.orderdetail.service.OrderDetailService;

/**
 * packageName    : store.mybooks.resource.order_detail.contorller<br>
 * fileName       : OrderDetailController<br>
 * author         : minsu11<br>
 * date           : 3/16/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/16/24        minsu11       최초 생성<br>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/order-details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/{orderNumber}")
    public ResponseEntity<List<OrderDetailInfoResponse>> get(@PathVariable String orderNumber) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderDetailService.getOrderDetails(orderNumber));
    }
}
