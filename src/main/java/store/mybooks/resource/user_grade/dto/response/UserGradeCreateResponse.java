package store.mybooks.resource.user_grade.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@AllArgsConstructor
public class UserGradeCreateResponse {

    private String name;

    private Integer minCost;

    private Integer maxCost;

    private Integer rate;

    private LocalDate createdDate;

}
