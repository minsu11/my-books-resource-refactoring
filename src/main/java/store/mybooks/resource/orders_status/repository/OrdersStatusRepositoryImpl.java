package store.mybooks.resource.orders_status.repository;

import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;
import store.mybooks.resource.orders_status.dto.response.QOrdersStatusResponse;
import store.mybooks.resource.orders_status.entity.QOrdersStatus;

/**
 * packageName    : store.mybooks.resource.orders_status.repository
 * fileName       : OrdersStatusRepositoryImpl
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
public class OrdersStatusRepositoryImpl extends QuerydslRepositorySupport implements OrdersStatusRepositoryCustom {

    public OrdersStatusRepositoryImpl() {
        super(OrdersStatusResponse.class);
    }

    @Override
    public List<OrdersStatusResponse> getOrdersStatusList() {
        QOrdersStatus qOrdersStatus = QOrdersStatus.ordersStatus;
        return from(qOrdersStatus)
                .select(new QOrdersStatusResponse(qOrdersStatus.id))
                .fetch();
    }
}
