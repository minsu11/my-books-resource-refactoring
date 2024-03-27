package store.mybooks.resource.bookorder.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.bookorder.dto.request.BookInfoRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderCreateRequest;
import store.mybooks.resource.bookorder.dto.response.BookOrderCreateResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.eumulation.BookOrderStatusName;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailCreateResponse;
import store.mybooks.resource.orderdetail.service.OrderDetailService;
import store.mybooks.resource.payment.dto.request.PayCancelRequest;
import store.mybooks.resource.payment.dto.request.PayCreateRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.service.PaymentService;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.pointrulename.enumulation.PointRuleNameEnum;

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
@Slf4j
@Service
@RequiredArgsConstructor
public class TotalOrderService {
    private final BookOrderService bookOrderService;
    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;
    private final PointHistoryService pointHistoryService;
    private final OrderCalculateService orderCalculateService;

    /**
     * methodName : createOrder<br>
     * author : minsu11<br>
     * description : 주문서를 만드는 통합 메서드.
     * <br> *
     *
     * @param request 등록할 도서 주문 정보
     * @param userId  the user id
     * @return the book order create response
     */
    @Transactional
    public BookOrderCreateResponse createOrder(BookOrderCreateRequest request, Long userId) {
        List<BookInfoRequest> bookorderInfoList = request.getBookInfoList();
        BookOrderCreateResponse bookOrder = bookOrderService.createBookOrder(request, userId);
        List<OrderDetailCreateResponse> orderDetailCreateResponseList =
                orderDetailService.createOrderDetailList(bookorderInfoList, request.getOrderNumber());
        Boolean isCouponUsed = bookOrderService.checkCouponUsed(orderDetailCreateResponseList);
        bookOrder.updateIsCouponUsed(isCouponUsed);
        return bookOrder;
    }

    /**
     * methodName : payUser<br>
     * author : minsu11<br>
     * description : 결제 승인 된 후 DB에 결제 정보 및 주문 쿠폰, 포인트, 재고 처리.
     * <br>
     *
     * @param request the request
     * @param userId  the user id
     * @return the pay create response
     */
    @Transactional
    public PayCreateResponse payUser(PayCreateRequest request, Long userId) {
        BookOrderInfoPayResponse bookOrderInfo = bookOrderService.getBookInfo(request.getOrderNumber());
        orderCalculateService.checkBookStock(bookOrderInfo.getOrderDetails());

        PayCreateResponse response = paymentService.createPayment(request, userId);
        orderCalculateService.calculateBookStock(bookOrderInfo.getOrderDetails(), BookOrderStatusName.ORDER_COMPLETED);
        orderCalculateService.useCouponProcessing(bookOrderInfo);
        orderCalculateService.pointProcessing(bookOrderInfo.getNumber(), bookOrderInfo.getPointCost(), userId, PointRuleNameEnum.USE_POINT);
        orderCalculateService.earnPoint(bookOrderInfo, userId);

        bookOrderService.updateBookOrderStatus(bookOrderInfo.getNumber(), BookOrderStatusName.ORDER_COMPLETED);
        return response;
    }

    /**
     * 주문 취소에 관련된 프로세스.
     *
     * @param request the request
     * @param userId  the user id
     */
    @Transactional
    public void cancelOrderProcess(PayCancelRequest request, Long userId) {
        BookOrderInfoPayResponse bookOrderInfo = bookOrderService.getBookInfo(request.getOrderNumber());
        bookOrderService.updateBookOrderStatus(bookOrderInfo.getNumber(), BookOrderStatusName.ORDER_CANCEL);
        orderCalculateService.calculateBookStock(bookOrderInfo.getOrderDetails(), BookOrderStatusName.ORDER_CANCEL);
        int usedPoint = pointHistoryService.getUsedPointOrder(request.getOrderNumber());
        int total = request.getTotalAmount();
        log.debug("사용한 포인트: {}", usedPoint);
        log.debug("총합 값: {}", total);
        int result = total - usedPoint;
        log.debug("결과값 : {}", total);
        paymentService.modifyStatus(request.getOrderNumber(), request.getStatus());
        orderCalculateService.pointProcessing(request.getOrderNumber(), result, userId, PointRuleNameEnum.RETURN_POINT);
    }


}