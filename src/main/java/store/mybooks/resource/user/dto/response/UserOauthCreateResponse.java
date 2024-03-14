package store.mybooks.resource.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.user.dto.response<br>
 * fileName       : UserOauthCreateResponse<br>
 * author         : masiljangajji<br>
 * date           : 3/14/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/14/24        masiljangajji       최초 생성
 */

@Getter
@Setter
@AllArgsConstructor
public class UserOauthCreateResponse {

    private String name;

    private String email;

    private Long id;

    private Integer birthYear;

    private String birthMonthDay;


    private String userStatusName;

    private String userGradeName;

}
