package store.mybooks.resource.book_order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.book_order.dto.mapper.BookOrderMapper;
import store.mybooks.resource.book_order.dto.request.BookOrderAdminModifyRequest;
import store.mybooks.resource.book_order.dto.response.BookOrderAdminModifyResponse;
import store.mybooks.resource.book_order.dto.response.BookOrderAdminResponse;
import store.mybooks.resource.book_order.dto.response.BookOrderUserResponse;
import store.mybooks.resource.book_order.entity.BookOrder;
import store.mybooks.resource.book_order.exception.BookOrderNotExistException;
import store.mybooks.resource.book_order.repository.BookOrderRepository;
import store.mybooks.resource.orders_status.entity.OrdersStatus;
import store.mybooks.resource.orders_status.enumulation.OrdersStatusEnum;
import store.mybooks.resource.orders_status.exception.OrdersStatusNotExistException;
import store.mybooks.resource.orders_status.repository.OrdersStatusRepository;

/**
 * packageName    : store.mybooks.resource.book_order.service<br>
 * fileName       : BookOrderService<br>
 * author         : minsu11<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/2/24        minsu11       최초 생성<br>
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookOrderService {
    private final BookOrderRepository bookOrderRepository;

    private final OrdersStatusRepository ordersStatusRepository;
    private final BookOrderMapper bookOrderMapper;

    /**
     * methodName : getBookOrderResponseList<br>
     * author : minsu11<br>
     * description : 회원아디로 조회한 후 회원의 주문 내역 목록 페이징
     * <br> *
     *
     * @param userId   조회할 회원 아이디
     * @param pageable 페이징 관련 정보
     * @return page
     */
    @Transactional(readOnly = true)
    public Page<BookOrderUserResponse> getBookOrderResponseList(Long userId, Pageable pageable) {

        return bookOrderRepository.getBookOrderPageByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<BookOrderAdminResponse> getBookOrderAdminResponseList(Pageable pageable) {
        return bookOrderRepository.getBookOrderPageByOrderStatusId(pageable);
    }

    /**
     * methodName : modifyBookOrderStatus<br>
     * author : minsu11<br>
     * description : 관리자가 주문 상태를 변경.
     * <br> *
     *
     * @param request 변경할 주문 상태가 들어있느 DTO
     * @return book order modify order status request
     */
    public BookOrderAdminModifyResponse modifyBookOrderAdminStatus(BookOrderAdminModifyRequest request) {
        log.info("enum value:{}", OrdersStatusEnum.DELIVERY.toString());
        OrdersStatus ordersStatus = ordersStatusRepository.findById(OrdersStatusEnum.DELIVERY.toString())
                .orElseThrow(OrdersStatusNotExistException::new);
        log.info("enum value:{}", ordersStatus.getId());
        log.info("request value:{}", request.getId());

        BookOrder bookOrder = bookOrderRepository.findById(request.getId()).orElseThrow(BookOrderNotExistException::new);
        bookOrder.modifyBookOrderAdmin(ordersStatus);
        return bookOrderMapper.mapToBookOrderModifyOrderStatusResponse(bookOrder);
    }
}
