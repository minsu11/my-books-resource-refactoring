package store.mybooks.resource.user.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_status.entity.UserStatus;

/**
 * packageName    : store.mybooks.resource.user.dto.response
 * fileName       : UserCreateResponse
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Builder
@Getter
public class UserCreateResponse {

    private String name;

    private String password;

    private String phoneNumber;

    private String email;

    private LocalDate birth;

    private UserStatus userStatusName;

    private UserGrade userGradeName;

    private LocalDateTime createdAt;


}
