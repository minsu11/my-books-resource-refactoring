package store.mybooks.resource.user_grade.dto.response;

import java.time.LocalDate;
import lombok.Data;

/**
 * packageName    : store.mybooks.resource.user_grade.dto.response
 * fileName       : UserGradeCreateResponse
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
@Data
public class UserGradeCreateResponse {

    private String name;

    private Integer minCost;

    private Integer maxCost;

    private Integer rate;

    private LocalDate createdDate;

}
