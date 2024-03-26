package store.mybooks.resource.bookorder.service;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.book.service.BookService;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.pointrule.service.PointRuleService;
import store.mybooks.resource.pointrulename.service.PointRuleNameService;
import store.mybooks.resource.user.repository.UserRepository;
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

    @BeforeEach
    void setUp() {
        orderDetailInfoResponse = new OrderDetailInfoResponse(1L, "test", 1L, 10000, 2, false, "test image", "test status", 1L);


    }

    @Test
    @DisplayName("재고 계산 처리 테스트")
    void givenOrderDetailInfoListAndBookOrderStatusName_whenUpdateBookStock() {
        List<OrderDetailInfoResponse> responses = List.of(orderDetailInfoResponse);
//        given(bookService.updateBookStock(any(), any(), any())).willReturn();
    }
}