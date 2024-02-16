package store.mybooks.resource.order_detail_status.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.order_detail_status.dto.request
 * fileName       : OrderDetailStatusRequest
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
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailStatusRequest {
    private String id;
}
