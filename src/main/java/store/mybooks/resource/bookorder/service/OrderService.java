package store.mybooks.resource.bookorder.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.bookorder.dto.request.BookInfoRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderCreateRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderInfoRequest;
import store.mybooks.resource.bookorder.dto.response.BookOrderCreateResponse;
import store.mybooks.resource.order_detail.dto.response.OrderDetailCreateResponse;
import store.mybooks.resource.order_detail.service.OrderDetailService;
import store.mybooks.resource.orderdetailstatus.service.OrderDetailStatusService;
import store.mybooks.resource.orders_status.service.OrdersStatusService;

/**
 * packageName    : store.mybooks.resource.bookorder.service<br>
 * fileName       : OrderService<br>
 * author         : minsu11<br>
 * date           : 3/16/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/16/24        minsu11       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    private final BookOrderService bookOrderService;
    private final OrdersStatusService ordersStatusService;
    private final OrderDetailStatusService orderDetailStatusService;
    private final OrderDetailService orderDetailService;

    /**
     * 주문서를 만드는 통합 메서드.
     *
     * @param request the request
     * @param userId  the user id
     * @return the book order create response
     */
    @Transactional
    public BookOrderCreateResponse createOrder(BookOrderCreateRequest request, Long userId) {
        List<BookInfoRequest> bookorderInfoList = request.getBookInfoList();
        BookOrderInfoRequest orderInfo = request.getOrderInfo();
        BookOrderCreateResponse bookOrder = bookOrderService.createBookOrder(request, userId);
        List<OrderDetailCreateResponse> orderDetailCreateResponseList =
                orderDetailService.createOrderDetailList(bookorderInfoList, request.getOrderNumber());
        return bookOrder;
    }
}