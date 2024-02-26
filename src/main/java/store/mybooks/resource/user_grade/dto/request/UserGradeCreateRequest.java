package store.mybooks.resource.user_grade.dto.request;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user_grade.dto.request
 * fileName       : UserGradeCreateRequest
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserGradeCreateRequest {

    private String name;

    private Integer minCost;

    private Integer maxCost;

    private Integer rate;

    private LocalDate createdDate;

}
