package store.mybooks.resource.order_detail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.order_detail.dto.response<br>
 * fileName       : OrderDetailInfoResponse<br>
 * author         : minsu11<br>
 * date           : 3/18/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/18/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class OrderDetailInfoResponse {
    private String bookName;
    private Integer cost;
    private Boolean isCouponUsed;
}
