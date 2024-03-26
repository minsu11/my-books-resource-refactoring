package store.mybooks.resource.bookorder.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.book.dto.response.BookStockResponse;
import store.mybooks.resource.book.service.BookService;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.eumulation.BookOrderStatusName;
import store.mybooks.resource.bookorder.exception.BookStockException;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryCreateResponse;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.pointrule.dto.response.PointRuleResponse;
import store.mybooks.resource.pointrule.service.PointRuleService;
import store.mybooks.resource.pointrulename.dto.response.PointRuleNameResponse;
import store.mybooks.resource.pointrulename.enumulation.PointRuleNameEnum;
import store.mybooks.resource.pointrulename.service.PointRuleNameService;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.enumeration.UserGradeNameEnum;
import store.mybooks.resource.usercoupon.service.UserCouponService;

/**
 * packageName    : store.mybooks.resource.bookorder.service<br>
 * fileName       : OrderCalculateServiceTest<br>
 * author         : minsu11<br>
 * date           : 3/26/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/26/24        minsu11       최초 생성<br>
 */
@ExtendWith({MockitoExtension.class})
class OrderCalculateServiceTest {
    @InjectMocks
    private OrderCalculateService orderCalculateService;

    @Mock
    private BookService bookService;

    @Mock
    private PointRuleNameService pointRuleNameService;

    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCouponService userCouponService;
    @Mock
    private PointRuleService pointRuleService;

    private OrderDetailInfoResponse orderDetailInfoResponse;
    private BookOrderInfoPayResponse bookOrderInfoPayResponse;

    @BeforeEach
    void setUp() {
        orderDetailInfoResponse = new OrderDetailInfoResponse(1L, "test", 1L, 10000, 2, true, "test image", "test status", 1L);
        bookOrderInfoPayResponse = new BookOrderInfoPayResponse("test", "testNumber", 1000,
                false, 1000, List.of(orderDetailInfoResponse));

    }

    @Test
    @DisplayName("재고 계산 처리 테스트")
    void givenOrderDetailInfoListAndBookOrderStatusName_whenUpdateBookStock() {
        List<OrderDetailInfoResponse> responses = List.of(orderDetailInfoResponse);

        orderCalculateService.calculateBookStock(responses, BookOrderStatusName.ORDER_COMPLETED);
        verify(bookService, times(responses.size())).updateBookStock(anyLong(), any(), any());
    }

    @Test
    @DisplayName("빈 리스트가 들어왔을 때 재고 계산 처리 테스트")
    void givenEmptyListAndBookOrderStatusName_whenUpdateBookStock() {
        orderCalculateService.calculateBookStock(Collections.emptyList(), BookOrderStatusName.ORDER_COMPLETED);
        verify(bookService, never()).updateBookStock(anyLong(), any(), any());
    }

    @Test
    @DisplayName("포인트 사용 처리 테스트")
    void givenOrderNumberAndPointAndPointRuleUsePointEnumAndUserId_whenCreatePointHistory() {
        PointHistoryCreateResponse pointHistoryResponse = new PointHistoryCreateResponse(1L);
        PointRuleNameResponse pointRuleName = new PointRuleNameResponse("test");
        given(pointRuleNameService.getPointRuleName(anyString())).willReturn(pointRuleName);
        given(pointHistoryService.createPointHistory(any(), anyLong())).willReturn(pointHistoryResponse);
        orderCalculateService.pointProcessing("testNumber", 1000, 1L, PointRuleNameEnum.USE_POINT);
        verify(pointHistoryService, times(1)).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("포인트 리뷰 처리 테스트")
    void givenOrderNumberAndPointAndPointRuleNameReviewAndUserId_whenCreatePointHistory() {
        PointHistoryCreateResponse pointHistoryResponse = new PointHistoryCreateResponse(1L);
        PointRuleNameResponse pointRuleName = new PointRuleNameResponse("test");
        given(pointRuleNameService.getPointRuleName(anyString())).willReturn(pointRuleName);
        given(pointHistoryService.createPointHistory(any(), anyLong())).willReturn(pointHistoryResponse);
        orderCalculateService.pointProcessing("testNumber", 1000, 1L, PointRuleNameEnum.SIGNUP_POINT);
        verify(pointHistoryService, times(1)).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 포인트 처리")
    void givenUserIdZero_thenReturn() {
        orderCalculateService.pointProcessing("test", 1000, 0L, PointRuleNameEnum.BOOK_POINT);
        verify(pointHistoryService, never()).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("포인트 0원 처리")
    void givenPointValueZero_thenReturn() {
        orderCalculateService.pointProcessing("test", 0, 2L, PointRuleNameEnum.BOOK_POINT);
        verify(pointHistoryService, never()).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("비회원 포인트 적립 테스트")
    void givenUserIdZero_thenReturnVoid() {
        orderCalculateService.earnPoint(bookOrderInfoPayResponse, 0L);
        verify(userRepository, never()).findById(any());
        verify(pointRuleService, never()).getPointRuleResponseByName(anyString());
        verify(pointHistoryService, never()).createPointHistory(any(), anyLong());
    }

    @Test
    @DisplayName("상품에 대한 포인트 적립 성공 테스트")
    void givenBookOrderInfoPayResponseAndUserId_whenCreatePointHistory() {

        PointRuleResponse pointRuleResponse = new PointRuleResponse(1, PointRuleNameEnum.BOOK_POINT.getValue(),
                1, null);

        User user = new User();
        UserGradeName userGradeName = new UserGradeName(UserGradeNameEnum.NORMAL.name());
        UserGrade userGrade = new UserGrade(1000, 10000, 1, userGradeName);
        user.modifyUserGrade(userGrade);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(pointRuleService.getPointRuleResponseByName(anyString())).willReturn(pointRuleResponse);
        orderCalculateService.earnPoint(bookOrderInfoPayResponse, 1L);

        verify(userRepository, times(1)).findById(any());
        verify(pointRuleService, times(1)).getPointRuleResponseByName(anyString());
    }

    @Test
    @DisplayName("유저가 없는 경우 포인트 적립 실패 테스트")
    void givenBookOrderInfoPayResponseAndUserId_whenGetPointRuleResponseByName_thenThrowUserNotExistException() {
        given(userRepository.findById(any())).willThrow(UserNotExistException.class);
        Assertions.assertThrows(UserNotExistException.class,
                () -> orderCalculateService.earnPoint(bookOrderInfoPayResponse, 1L));
        verify(userRepository, times(1)).findById(any());
        verify(pointRuleService, never()).getPointRuleResponseByName(anyString());
    }

    @Test
    @DisplayName("책 주문 량과 재고 량 비교 체크 테스트")
    void givenOrderDetailInfoList_whenGetBookStockResponse() {
        List<OrderDetailInfoResponse> responses = List.of(orderDetailInfoResponse);
        BookStockResponse stockResponse = new BookStockResponse(1L, 10);
        given(bookService.getBookStockResponse(anyLong())).willReturn(stockResponse);
        orderCalculateService.checkBookStock(responses);

        verify(bookService, times(responses.size())).getBookStockResponse(anyLong());
    }

    @Test
    @DisplayName("책 주문량이 재고량 보다 많은 경우 throw 테스트")
    void givenorderDetailInfoList_whenGetBookResponse_thenThrowBookStockException() {
        List<OrderDetailInfoResponse> responses = List.of(orderDetailInfoResponse);
        BookStockResponse stockResponse = new BookStockResponse(1L, 1);
        given(bookService.getBookStockResponse(anyLong())).willReturn(stockResponse);
        Assertions.assertThrows(BookStockException.class, () -> orderCalculateService.checkBookStock(responses));

        verify(bookService, times(1)).getBookStockResponse(anyLong());

    }

    @Test
    @DisplayName("사용된 개별 쿠폰에 대한 처리")
    void givenBookOrderInfoPayResponse_whenGetOrderDetails() {
        orderCalculateService.useCouponProcessing(bookOrderInfoPayResponse);
        verify(userCouponService, times(1)).useUserCoupon(anyLong());
    }

    @Test
    @DisplayName("사용하지 않은 개별 쿠폰에 대한 처리")
    void givenBookOrderInfoPayResponse_whenGetIsCouponUsed_thenReturnFalse() {
        OrderDetailInfoResponse detailInfoResponse = new OrderDetailInfoResponse(1L, "test", 2L, 10000, 2, false, "test image", "test status", 1L);
        BookOrderInfoPayResponse orderInfoPayResponse = new BookOrderInfoPayResponse("test", "testNumber", 1000,
                false, 1000, List.of(detailInfoResponse));

        orderCalculateService.useCouponProcessing(orderInfoPayResponse);

        verify(userCouponService, never()).useUserCoupon(any());
    }
}