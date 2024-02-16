package store.mybooks.resource.order_detail_status.repository;

import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.order_detail_status.dto.response.OrderDetailStatusResponse;
import store.mybooks.resource.order_detail_status.dto.response.QOrderDetailStatusResponse;
import store.mybooks.resource.order_detail_status.entity.QOrderDetailStatus;

/**
 * packageName    : store.mybooks.resource.order_detail_status.repository
 * fileName       : OrderDetailStatusRepositoryImpl
 * author         : minsu11
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        minsu11       최초 생성
 */
public class OrderDetailStatusRepositoryImpl extends QuerydslRepositorySupport implements OrderDetailStatusRepositoryCustom {
    public OrderDetailStatusRepositoryImpl() {
        super(OrderDetailStatusResponse.class);
    }

    @Override
    public List<OrderDetailStatusResponse> getOrderDetailStatusResponseList() {
        QOrderDetailStatus orderDetailStatus = QOrderDetailStatus.orderDetailStatus;
        return from(orderDetailStatus)
                .select(new QOrderDetailStatusResponse(orderDetailStatus.id))
                .fetch();
    }
}
