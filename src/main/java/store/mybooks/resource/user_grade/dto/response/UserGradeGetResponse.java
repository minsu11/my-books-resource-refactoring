package store.mybooks.resource.user_grade.dto.response;

import java.time.LocalDate;

/**
 * packageName    : store.mybooks.resource.user_grade.dto.response<br>
 * fileName       : UserGradeGetResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public interface UserGradeGetResponse {
    String getUserGradeNameId();

    Integer getMinCost();

    Integer getMaxCost();

    Integer getRate();

    LocalDate getCreatedDate();

}
