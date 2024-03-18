package store.mybooks.resource.order_detail.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import store.mybooks.resource.order_detail.dto.response.OrderDetailInfoResponse;

/**
 * packageName    : store.mybooks.resource.order_detail.repository<br>
 * fileName       : OrderDetailRepositoryCutstom<br>
 * author         : minsu11<br>
 * date           : 3/18/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/18/24        minsu11       최초 생성<br>
 */
@NoRepositoryBean
public interface OrderDetailRepositoryCustom {
    List<OrderDetailInfoResponse> getOrderDetailList(Long bookOrderId);


}
