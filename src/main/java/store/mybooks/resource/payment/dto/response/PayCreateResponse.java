package store.mybooks.resource.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.payment.dto.response<br>
 * fileName       : PayCreateResponse<br>
 * author         : minsu11<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/19/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class PayCreateResponse {
    private Long payId;
    private String paymentKey;
    private Integer totalAmount;
}
