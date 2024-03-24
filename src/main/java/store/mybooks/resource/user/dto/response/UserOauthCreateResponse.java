package store.mybooks.resource.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
