package store.mybooks.resource.delivery_rule.dto;

import java.time.LocalDate;


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

public interface DeliveryRuleResponse {

    Integer getId();

    Integer getDeliveryNameRuleId();

    String getCompanyName();

    Integer getCost();

    Integer getRuleCost();

    LocalDate getCreatedDate();

    Boolean getIsAvailable();
}
