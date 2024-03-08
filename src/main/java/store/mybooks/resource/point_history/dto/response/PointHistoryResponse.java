package store.mybooks.resource.point_history.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.point_history.dto.response<br>
 * fileName       : PointHistoryResponse<br>
 * author         : minsu11<br>
 * date           : 3/8/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/8/24        minsu11       최초 생성<br>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointHistoryResponse {
    private String orderNumber;
    private String pointRuleName;
    private Integer statusCost;
    private LocalDate createdDate;
}
