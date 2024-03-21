package store.mybooks.resource.bookorder.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book.dto.response.BookStockResponse;
import store.mybooks.resource.book.service.BookService;
import store.mybooks.resource.bookorder.dto.request.BookInfoRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderCreateRequest;
import store.mybooks.resource.bookorder.dto.response.BookOrderCreateResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.eumulation.BookOrderStatusName;
import store.mybooks.resource.bookorder.exception.BookStockException;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailCreateResponse;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.orderdetail.service.OrderDetailService;
import store.mybooks.resource.orderdetailstatus.service.OrderDetailStatusService;
import store.mybooks.resource.ordersstatus.service.OrdersStatusService;
import store.mybooks.resource.payment.dto.request.PayCreateRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.service.PaymentService;
import store.mybooks.resource.pointhistory.dto.request.PointHistoryCreateRequest;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.pointrule.dto.response.PointRuleResponse;
import store.mybooks.resource.pointrule.service.PointRuleService;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameResponse;
import store.mybooks.resource.pointrulename.enumulation.PointRuleNameEnum;
import store.mybooks.resource.pointrulename.service.PointRuleNameService;
import store.mybooks.resource.usercoupon.service.UserCouponService;

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
    private final BookService bookService;
    private final BookOrderService bookOrderService;
    private final OrdersStatusService ordersStatusService;
    private final OrderDetailStatusService orderDetailStatusService;
    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;
    private final PointHistoryService pointHistoryService;
    private final PointRuleNameService pointRuleNameService;
    private final PointRuleService pointRuleService;
    private final UserCouponService userCouponService;


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
        BookOrderCreateResponse bookOrder = bookOrderService.createBookOrder(request, userId);
        List<OrderDetailCreateResponse> orderDetailCreateResponseList =
                orderDetailService.createOrderDetailList(bookorderInfoList, request.getOrderNumber());
        Boolean isCouponUsed = bookOrderService.checkCouponUsed(orderDetailCreateResponseList);
        bookOrder.updateIsCouponUsed(isCouponUsed);
        return bookOrder;
    }

    /**
     * 결제 승인 된 후 DB에 결제 정보 및 주문 쿠폰, 포인트에 대한 처리.
     *
     * @param request the request
     * @param userId  the user id
     * @return the pay create response
     */
    @Transactional
    public PayCreateResponse payUser(PayCreateRequest request, Long userId) {
        BookOrderInfoPayResponse bookOrderInfo = bookOrderService.getBookInfo(request.getOrderNumber());
        log.debug("재고 확인 전");
        checkBookStock(bookOrderInfo.getOrderDetails());
        log.debug("재고 확인 후");
        PayCreateResponse response = paymentService.createPayment(request);
        //재고 처리
        calculateBookStock(bookOrderInfo.getOrderDetails());
        // 쿠폰 처리
        useCouponProcessing(bookOrderInfo);
        log.debug("쿠폰 확인 후");
        usePointProcessing(bookOrderInfo, userId);
        earnPoint(bookOrderInfo, userId);
        // 주문 상태 변경
        bookOrderService.updateBookOrderStatus(bookOrderInfo.getNumber(), BookOrderStatusName.ORDER_COMPLETED);
        return response;
    }


    /**
     * Use coupon processing.
     *
     * @param bookOrder the book order
     */
    public void useCouponProcessing(BookOrderInfoPayResponse bookOrder) {
        if (bookOrder.getIsCouponUsed()) { // 전체 쿠폰 사용 했을 경우

        } else {
            bookOrder.getOrderDetails()
                    .stream()
                    .filter(OrderDetailInfoResponse::getIsCouponUsed)
                    .forEach(order -> userCouponService.useUserCoupon(order.getCouponId()));
        }
    }

    /**
     * Earn point.
     *
     * @param bookOrder the book order
     * @param userId    the user id
     */
    public void earnPoint(BookOrderInfoPayResponse bookOrder, Long userId) {
        PointRuleResponse pointRule = pointRuleService
                .getPointRuleResponseByName(PointRuleNameEnum.BOOK_POINT.getValue());
        int earnPoint = (bookOrder.getTotalCost() * pointRule.getRate()) / 100;
        PointHistoryCreateRequest point =
                new PointHistoryCreateRequest(bookOrder.getNumber(), pointRule.getPointRuleName(), earnPoint);
        pointHistoryService.createPointHistory(point, userId);
    }

    /**
     * 사용한 포인트 처리.
     *
     * @param bookOrder the book order
     * @param userId    the user id
     */
    public void usePointProcessing(BookOrderInfoPayResponse bookOrder, Long userId) {
        if (bookOrder.getPointCost() > 0) {
            PointRuleNameResponse pointRuleName = pointRuleNameService
                    .getPointRuleName(PointRuleNameEnum.USE_POINT.getValue());
            PointHistoryCreateRequest point = new PointHistoryCreateRequest(bookOrder.getNumber(),
                    pointRuleName.getId(), bookOrder.getPointCost());
            pointHistoryService.createPointHistory(point, userId);
        }
    }

    /**
     * 책 주문량과 재고 량 비교 체크.
     *
     * @param orderDetailInfoList the order detail info list
     */
    public void checkBookStock(List<OrderDetailInfoResponse> orderDetailInfoList) {
        for (OrderDetailInfoResponse orderDetail : orderDetailInfoList) {
            BookStockResponse bookStockResponse = bookService.getBookStockResponse(orderDetail.getId());
            if (orderDetail.getAmount() > bookStockResponse.getStock()) {
                throw new BookStockException();
            }
        }
    }

    /**
     * Calculate book stock.
     *
     * @param orderDetailInfoList the order detail info list
     */
    public void calculateBookStock(List<OrderDetailInfoResponse> orderDetailInfoList) {
        for (OrderDetailInfoResponse orderDetail : orderDetailInfoList) {
            bookService.updateBookStock(orderDetail.getId(), orderDetail.getAmount());
        }
    }

}