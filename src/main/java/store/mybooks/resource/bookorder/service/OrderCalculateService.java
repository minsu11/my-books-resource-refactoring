package store.mybooks.resource.bookorder.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book.dto.response.BookStockResponse;
import store.mybooks.resource.book.service.BookService;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.eumulation.BookOrderStatusName;
import store.mybooks.resource.bookorder.exception.BookStockException;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.pointhistory.dto.request.PointHistoryCreateRequest;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.pointrule.dto.response.PointRuleResponse;
import store.mybooks.resource.pointrule.service.PointRuleService;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameResponse;
import store.mybooks.resource.pointrulename.enumulation.PointRuleNameEnum;
import store.mybooks.resource.pointrulename.service.PointRuleNameService;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.usercoupon.service.UserCouponService;

/**
 * packageName    : store.mybooks.resource.bookorder.service<br>
 * fileName       : OrderCalculateService<br>
 * author         : minsu11<br>
 * date           : 3/25/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/25/24        minsu11       최초 생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderCalculateService {
    private final BookService bookService;
    private final PointRuleNameService pointRuleNameService;
    private final PointHistoryService pointHistoryService;
    private final UserRepository userRepository;
    private final PointRuleService pointRuleService;
    private final UserCouponService userCouponService;

    /**
     * methodName : calculateBookStock<br>
     * author : minsu11<br>
     * description : 재고 계산 처리.
     * <br>
     *
     * @param orderDetailInfoList the order detail info list
     */
    public void calculateBookStock(List<OrderDetailInfoResponse> orderDetailInfoList,
                                   BookOrderStatusName bookOrderStatusName) {

        for (OrderDetailInfoResponse orderDetail : orderDetailInfoList) {
            bookService.updateBookStock(orderDetail.getId(), orderDetail.getAmount(), bookOrderStatusName);
        }
    }

    /**
     * methodName : pointProcessing<br>
     * author : minsu11<br>
     * description : 포인트 사용 및 상품 적립 외의 포인트 적립 처리.
     * <br>
     *
     * @param orderNumber the book order
     * @param pointValue
     * @param userId      the user id
     */
    public void pointProcessing(String orderNumber, Integer pointValue,
                                Long userId, PointRuleNameEnum pointRuleNameEnum) {
        PointRuleNameResponse pointRuleName;
        PointHistoryCreateRequest point;
        if (userId == 0 || pointValue <= 0) {
            return;
        } else {
            pointRuleName = pointRuleNameService
                    .getPointRuleName(pointRuleNameEnum.getValue());
        }

        if (pointRuleNameEnum == PointRuleNameEnum.USE_POINT) {
            point = new PointHistoryCreateRequest(orderNumber,
                    pointRuleName.getId(), -pointValue);
        } else {
            point = new PointHistoryCreateRequest(orderNumber,
                    pointRuleName.getId(), pointValue);
        }
        pointHistoryService.createPointHistory(point, userId);
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

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));

        int userGradeRate = user.getUserGrade().getRate();

        PointRuleResponse pointRule = pointRuleService
                .getPointRuleResponseByName(PointRuleNameEnum.BOOK_POINT.getValue());

        int earnPoint = ((bookOrder.getTotalCost() * userGradeRate) / 100)
                + ((bookOrder.getTotalCost() * pointRule.getRate()) / 100);
        PointHistoryCreateRequest point =
                new PointHistoryCreateRequest(bookOrder.getNumber(), pointRule.getPointRuleName(), earnPoint);
        pointHistoryService.createPointHistory(point, userId);
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
     * methodName : useCouponProcessing<br>
     * author : minsu11<br>
     * description : 개별 쿠폰에 대한 처리.
     * <br>
     *
     * @param bookOrder the book order
     */
    public void useCouponProcessing(BookOrderInfoPayResponse bookOrder) {
        bookOrder.getOrderDetails()
                .stream()
                .filter(OrderDetailInfoResponse::getIsCouponUsed)
                .forEach(order -> userCouponService.useUserCoupon(order.getCouponId()));

    }
}
