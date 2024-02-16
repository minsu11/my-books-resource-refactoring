package store.mybooks.resource.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_status.entity.UserStatus;

/**
 * packageName    : store.mybooks.resource.user.dto.request
 * fileName       : UserModifyRequest
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Getter
public class UserModifyRequest {

    private String name;
    private String email;
    private String password;

    private UserStatus userStatusName;
    private UserGrade userGradeName;


}
