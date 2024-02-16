package store.mybooks.resource.orders_status.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.orders_status.dto.response
 * fileName       : OrdersStatusResponse
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
@Getter
@Builder
public class OrdersStatusResponse {
    private String id;

    @QueryProjection
    public OrdersStatusResponse(String id) {
        this.id = id;
    }
}
