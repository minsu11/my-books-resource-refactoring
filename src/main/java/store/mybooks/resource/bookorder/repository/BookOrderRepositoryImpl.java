package store.mybooks.resource.bookorder.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.bookorder.dto.response.BookOrderAdminResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderUserResponse;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.entity.QBookOrder;
import store.mybooks.resource.orders_status.enumulation.OrdersStatusEnum;

/**
 * packageName    : store.mybooks.resource.book_order.repository<br>
 * fileName       : BookOrderRepositoryImpl<br>
 * author         : minsu11<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/2/24        minsu11       최초 생성<br>
 */
public class BookOrderRepositoryImpl extends QuerydslRepositorySupport implements BookOrderRepositoryCustom {
    private static final QBookOrder bookOrder = QBookOrder.bookOrder;

    public BookOrderRepositoryImpl() {
        super(BookOrder.class);
    }


    @Override
    public Page<BookOrderUserResponse> getBookOrderPageByUserId(Long userId, Pageable pageable) {

        List<BookOrderUserResponse> bookOrderResponseList =
                from(bookOrder)
                        .select(Projections.constructor(BookOrderUserResponse.class,
                                bookOrder.user.id,
                                bookOrder.orderStatus.id,
                                bookOrder.deliveryRule.id,
                                bookOrder.date,
                                bookOrder.invoiceNumber,
                                bookOrder.receiverName,
                                bookOrder.receiverAddress,
                                bookOrder.receiverPhoneNumber,
                                bookOrder.receiverMessage,
                                bookOrder.totalCost,
                                bookOrder.pointCost,
                                bookOrder.couponCost,
                                bookOrder.number,
                                bookOrder.findPassword))
                        .where(bookOrder.user.id.eq(userId))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long count = from(bookOrder).fetchCount();

        return new PageImpl<>(bookOrderResponseList, pageable, count);
    }

    @Override
    public Page<BookOrderAdminResponse> getBookOrderPageByOrderStatusId(Pageable pageable) {
        List<BookOrderAdminResponse> bookOrderAdminResponseList =
                from(bookOrder)
                        .select(Projections.constructor(BookOrderAdminResponse.class,
                                bookOrder.id,
                                bookOrder.user.id,
                                bookOrder.orderStatus.id,
                                bookOrder.date,
                                bookOrder.outDate,
                                bookOrder.invoiceNumber,
                                bookOrder.number))
                        .where(bookOrder.orderStatus.id.eq(OrdersStatusEnum.WAIT.toString()))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();
        long count = bookOrderAdminResponseList.size();

        return new PageImpl<>(bookOrderAdminResponseList, pageable, count);
    }
}
