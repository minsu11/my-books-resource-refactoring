package store.mybooks.resource.return_rule_name.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * packageName    : store.mybooks.resource.return_rule_name.dto.request<br>
 * fileName       : ReturnRulsNameCreateRequest<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRuleNameCreateRequest {

    @NotBlank
    @Length(min = 2, max = 10)
    private String id;
}
