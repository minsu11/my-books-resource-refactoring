package store.mybooks.resource.orders_status.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.orders_status.dto.request
 * fileName       : OrdersStatusRequest
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
@Getter
public class OrdersStatusRequest {
    @NotBlank
    private String id;
}
