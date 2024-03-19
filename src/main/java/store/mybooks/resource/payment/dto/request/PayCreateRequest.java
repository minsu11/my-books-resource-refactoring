package store.mybooks.resource.payment.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.payment.dto.request<br>
 * fileName       : PayCreateRequest<br>
 * author         : minsu11<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/19/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class PayCreateRequest {
    private String orderNumber;
    private String status;
    private LocalDateTime requestAt;
    private Integer totalCost;
    private String type;
}
