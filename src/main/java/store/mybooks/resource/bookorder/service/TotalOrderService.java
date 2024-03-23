package store.mybooks.resource.bookorder.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
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

        checkBookStock(bookOrderInfo.getOrderDetails());

        PayCreateResponse response = paymentService.createPayment(request);
        calculateBookStock(bookOrderInfo.getOrderDetails());

        useCouponProcessing(bookOrderInfo);
        usePointProcessing(bookOrderInfo, userId);
        earnPoint(bookOrderInfo, userId);
        bookOrderService.updateBookOrderStatus(bookOrderInfo.getNumber(), BookOrderStatusName.ORDER_COMPLETED);
        return response;
    }


    /**
     * methodName : useCouponProcessing<br>
     * author : minsu11<br>
     * description : 개별이거나 전체 쿠폰에 대한 처리.
     * <br>
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
     * methodName : earnPoint<br>
     * author : minsu11<br>
     * description : 포인트 적립 규정에 따른 포인트 적립.
     * <br>
     *
     * @param bookOrder the book order
     * @param userId    the user id
     */
    public void earnPoint(BookOrderInfoPayResponse bookOrder, Long userId) {
        if (userId == 0L) {
            return;
        }
        PointRuleResponse pointRule = pointRuleService
                .getPointRuleResponseByName(PointRuleNameEnum.BOOK_POINT.getValue());
        int earnPoint = (bookOrder.getTotalCost() * pointRule.getRate()) / 100;
        PointHistoryCreateRequest point =
                new PointHistoryCreateRequest(bookOrder.getNumber(), pointRule.getPointRuleName(), earnPoint);
        pointHistoryService.createPointHistory(point, userId);
    }

    /**
     * methodName : usePointProcessing<br>
     * author : minsu11<br>
     * description : 포인트 사용 처리.
     * <br>
     *
     * @param bookOrder the book order
     * @param userId    the user id
     */
    public void usePointProcessing(BookOrderInfoPayResponse bookOrder, Long userId) {
        if (bookOrder.getPointCost() > 0 || userId != 0) {
            PointRuleNameResponse pointRuleName = pointRuleNameService
                    .getPointRuleName(PointRuleNameEnum.USE_POINT.getValue());
            PointHistoryCreateRequest point = new PointHistoryCreateRequest(bookOrder.getNumber(),
                    pointRuleName.getId(), bookOrder.getPointCost());
            pointHistoryService.createPointHistory(point, userId);
        }
    }

    /**
     * methodName : usePointProcessing<br>
     * author : minsu11<br>
     * description :  책 주문량과 재고 량 비교 체크.
     * <br>
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
     * methodName : calculateBookStock<br>
     * author : minsu11<br>
     * description : 재고 계산 처리.
     * <br>
     *
     * @param orderDetailInfoList the order detail info list
     */

    public void calculateBookStock(List<OrderDetailInfoResponse> orderDetailInfoList) {
        for (OrderDetailInfoResponse orderDetail : orderDetailInfoList) {
            bookService.updateBookStock(orderDetail.getId(), orderDetail.getAmount());
        }
    }

}