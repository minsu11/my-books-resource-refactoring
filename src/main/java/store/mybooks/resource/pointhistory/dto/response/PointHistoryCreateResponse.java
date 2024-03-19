package store.mybooks.resource.pointhistory.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.pointhistory.dto.response<br>
 * fileName       : PointHistoryCreateResponse<br>
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
public class PointHistoryCreateResponse {
    private String orderNumber;
    private String orderName;
    private String pointName;
    private Integer pointCost;
    private LocalDate createdAt;
}
