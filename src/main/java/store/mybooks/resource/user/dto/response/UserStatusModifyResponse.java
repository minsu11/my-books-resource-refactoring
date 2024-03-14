package store.mybooks.resource.user.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.user.dto.response<br>
 * fileName       : UserStatusModifyResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/25/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/25/24        masiljangajji       최초 생성
 */
@Getter
@Setter
@AllArgsConstructor
public class UserStatusModifyResponse {

    private String userStatusName;

    private LocalDate gradeChangedDate;

}
