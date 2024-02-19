package store.mybooks.resource.return_name_rule.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.return_name_rule.dto.response<br>
 * fileName       : ReturnNameRuleResponse<br>
 * author         : minsu11<br>
 * date           : 2/20/24<br>
 * description    : 요청된 반품 규정 명 규칙 응답 데이터 담은 dto<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/20/24        minsu11       최초 생성<br>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnNameRuleResponse {
    private String name;
    private LocalDate localDate;
}
