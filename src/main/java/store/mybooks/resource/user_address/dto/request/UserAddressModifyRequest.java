package store.mybooks.resource.user_address.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user_address.dto.request<br>
 * fileName       : UserAddressModifyRequest<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressModifyRequest {

    @NotBlank
    private String alias;

    @NotBlank
    private String detail;

    @NotBlank
    private String reference;


}
