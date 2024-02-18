package store.mybooks.resource.delivery_name_rule.dto;

import java.time.LocalDate;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.dto
 * fileName       : DeliveryNameRuleDto
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
public interface DeliveryNameRuleDto {
    Integer getId();

    String getName();

    LocalDate getCreatedDate();
}