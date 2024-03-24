package store.mybooks.resource.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.user.dto.response<br>
 * fileName       : UserCreateResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Getter
@AllArgsConstructor
public class UserCreateResponse {

    private String name;

    private String email;


    private Integer birthYear;

    private String birthMonthDay;


    private String userStatusName;

    private String userGradeName;


}
