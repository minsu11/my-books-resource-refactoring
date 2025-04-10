package store.mybooks.resource.user.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.front.user.dto.request<br>
 * fileName       : UserSocialModifyRequest<br>
 * author         : masiljangajji<br>
 * date           : 3/22/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/22/24        masiljangajji       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserOauthRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotNull
    @PastOrPresent
    private LocalDate birth;

    @NotBlank
    private String oauthId;

}
