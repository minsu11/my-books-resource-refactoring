package store.mybooks.resource.user_grade.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.user_grade.dto.response<br>
 * fileName       : UserGradeCreateResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
@Getter
@AllArgsConstructor
public class UserGradeCreateResponse {

    private String name;

    private Integer minCost;

    private Integer maxCost;

    private Integer rate;

    private LocalDate createdDate;

}
