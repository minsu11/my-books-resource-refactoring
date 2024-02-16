package store.mybooks.resource.user.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user.dto.request
 * fileName       : UserCreateRequest
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Getter
public class UserCreateRequest {

    private String userName;

    private String password;

    private String phoneNumber;

    private String email;

    private LocalDate birth;

    private Boolean isAdmin;


}
