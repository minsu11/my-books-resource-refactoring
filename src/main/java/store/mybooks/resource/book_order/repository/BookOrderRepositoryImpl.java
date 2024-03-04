package store.mybooks.resource.book_order.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.book_order.dto.response.BookOrderAdminResponse;
import store.mybooks.resource.book_order.dto.response.BookOrderUserResponse;
import store.mybooks.resource.book_order.entity.BookOrder;
import store.mybooks.resource.book_order.entity.QBookOrder;
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
    private final QBookOrder bookOrder = QBookOrder.bookOrder;

    public BookOrderRepositoryImpl() {
        super(BookOrder.class);
    }


    @Override
    public Page<BookOrderUserResponse> getBookOrderPageByUserId(Long userId, Pageable pageable) {

        List<BookOrderUserResponse> bookOrderResponseList = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable,
                        from(bookOrder)
                                .select(Projections.constructor(BookOrderUserResponse.class,
                                        bookOrder.user.id,
                                        bookOrder.orderStatus.id,
                                        bookOrder.deliveryRule.id,
                                        bookOrder.wrap.id,
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
                                .where(bookOrder.user.id.eq(userId)))
                .fetch();

        long count = from(bookOrder).fetchCount();

        return new PageImpl<>(bookOrderResponseList, pageable, count);
    }

    @Override
    public Page<BookOrderAdminResponse> getBookOrderPageByOrderStatusId(Pageable pageable) {
        List<BookOrderAdminResponse> bookOrderAdminResponseList =
                getQuerydsl().applyPagination(pageable, from(bookOrder)
                                .select(Projections.constructor(BookOrderAdminResponse.class,
                                        bookOrder.user.id,
                                        bookOrder.orderStatus.id,
                                        bookOrder.date,
                                        bookOrder.outDate,
                                        bookOrder.invoiceNumber,
                                        bookOrder.number))
                                .where(bookOrder.orderStatus.id.eq(OrdersStatusEnum.WAIT.toString())))
                        .fetch();
        long count = bookOrderAdminResponseList.size();

        return new PageImpl<>(bookOrderAdminResponseList, pageable, count);
    }
}
