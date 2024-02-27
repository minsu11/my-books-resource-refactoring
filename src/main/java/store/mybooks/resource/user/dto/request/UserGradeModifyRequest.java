package store.mybooks.resource.user.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user.dto.request<br>
 * fileName       : UserGradeModifyRequest<br>
 * author         : masiljangajji<br>
 * date           : 2/25/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/25/24        masiljangajji       최초 생성
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserGradeModifyRequest {
    private String userGradeName;

}
