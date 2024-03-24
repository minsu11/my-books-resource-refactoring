package store.mybooks.resource.user.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : store.mybooks.resource.user.dto.response<br>
 * fileName       : UserGetResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/16/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        masiljangajji       최초 생성
 */


public interface UserGetResponse {

    String getUserGradeUserGradeNameId();

    String getUserStatusId();

    String getName();

    String getPhoneNumber();

    String getEmail();

    Integer getBirthYear();

    String getBirthMonthDay();

    LocalDateTime getCreatedAt();

    LocalDateTime getLatestLogin();

    LocalDate getGradeChangedDate();


}
