package store.mybooks.resource.orders_status.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.orders_status.dto.response
 * fileName       : OrdersStatusCreateResponse
 * author         : minsu11
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        minsu11       최초 생성
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersStatusCreateResponse {
    private String id;
}
