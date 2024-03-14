package store.mybooks.resource.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.user.dto.response<br>
 * fileName       : UserModifyResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */


@Getter
@Setter
@AllArgsConstructor
public class UserModifyResponse {

    private String name;

    private String phoneNumber;

}
