package store.mybooks.resource.delivery_rule.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * packageName    : store.mybooks.resource.delivery_rule.dto
 * fileName       : DeliveryRuleResponse
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
public class DeliveryRuleResponse {

    private Integer id;

    private Integer deliveryNameRuleId;

    private String companyName;

    private Integer cost;

    private Integer ruleCost;

    private LocalDate createdDate;

    private Boolean isAvailable;
}
