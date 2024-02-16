package store.mybooks.resource.delivery_name_rule.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.dto
 * fileName       : DeliveryNameRuleReadResponse
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNameRuleResponse {
    private Integer id;
    private String name;
    private LocalDate createdDate;
}