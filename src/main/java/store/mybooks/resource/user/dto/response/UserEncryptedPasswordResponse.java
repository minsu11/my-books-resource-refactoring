package store.mybooks.resource.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.user.dto.response<br>
 * fileName       : UserEmailResponse<br>
 * author         : masiljangajji<br>
 * date           : 3/6/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/6/24        masiljangajji       최초 생성
 */
@AllArgsConstructor
@Getter
public class UserEncryptedPasswordResponse {

    private String encryptedPassword;

}
