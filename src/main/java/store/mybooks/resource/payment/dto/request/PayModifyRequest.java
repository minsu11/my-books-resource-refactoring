package store.mybooks.resource.payment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.payment.dto.request<br>
 * fileName       : PayModifyRequest<br>
 * author         : minsu11<br>
 * date           : 3/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/20/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class PayModifyRequest {
    private String status;
    private String orderNumber;
}
