package store.mybooks.resource.orders_status.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;

/**
 * packageName    : store.mybooks.resource.orders_status.repository
 * fileName       : OrdersStatusRepositoryCustom
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
@NoRepositoryBean
public interface OrdersStatusRepositoryCustom {
    List<OrdersStatusResponse> getOrdersStatusList();
}
