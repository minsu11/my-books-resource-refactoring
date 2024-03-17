package store.mybooks.resource.order_detail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.order_detail.dto.request<br>
 * fileName       : OrderDetailCreateRequest<br>
 * author         : minsu11<br>
 * date           : 3/16/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/16/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class OrderDetailCreateRequest {
    private String orderNumber;
    private Long bookId;
    private Long couponId;
    private Integer wrapId;
    private Integer bookCost;
    private Integer bookAmount;
}
