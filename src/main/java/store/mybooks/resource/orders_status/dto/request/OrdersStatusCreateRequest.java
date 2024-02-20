package store.mybooks.resource.orders_status.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.orders_status.dto.request
 * fileName       : OrdersStatusCreateRequest
 * author         : minsu11
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        minsu11       최초 생성
 */
@Getter
public class OrdersStatusCreateRequest {
    @NotBlank
    private String id;
}
