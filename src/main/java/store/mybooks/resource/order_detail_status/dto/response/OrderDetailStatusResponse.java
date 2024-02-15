package store.mybooks.resource.order_detail_status.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.order_detail_status.dto.response
 * fileName       : OrderDetailStatusResponse
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
public class OrderDetailStatusResponse {
    private String id;

    @QueryProjection
    public OrderDetailStatusResponse(String id) {
        this.id = id;
    }
}
