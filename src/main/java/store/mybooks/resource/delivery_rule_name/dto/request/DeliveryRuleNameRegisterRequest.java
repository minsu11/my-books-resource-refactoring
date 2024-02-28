package store.mybooks.resource.delivery_rule_name.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.dto<br>
 * fileName       : DeliveryNameRuleRegisterRequest<br>
 * author         : Fiat_lux<br>
 * date           : 2/15/24<br>
 * description    :<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/15/24        Fiat_lux       최초 생성<br>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRuleNameRegisterRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String id;
}