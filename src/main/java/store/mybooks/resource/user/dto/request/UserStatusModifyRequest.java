package store.mybooks.resource.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user.dto.request
 * fileName       : UserStatusModifyRequest
 * author         : masiljangajji
 * date           : 2/25/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/25/24        masiljangajji       최초 생성
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusModifyRequest {
    private String userStatusName;

}
