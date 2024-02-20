package store.mybooks.resource.user_grade.dto.response;

import java.time.LocalDate;
import javax.persistence.Column;

/**
 * packageName    : store.mybooks.resource.user_grade.dto.response
 * fileName       : UserGradeGetResponse
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public interface UserGradeGetResponse {


    String getName();


    Integer getMinCost();

    Integer getMaxCost();

    Integer getRate();

    LocalDate getCreatedDate();

}
