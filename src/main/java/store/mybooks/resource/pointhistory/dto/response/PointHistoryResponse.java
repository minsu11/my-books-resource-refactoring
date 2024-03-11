package store.mybooks.resource.pointhistory.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
@AllArgsConstructor
public class PointHistoryResponse {
    private String orderNumber;
    private String pointRuleName;
    private Integer statusCost;
    private LocalDate createdDate;
}
