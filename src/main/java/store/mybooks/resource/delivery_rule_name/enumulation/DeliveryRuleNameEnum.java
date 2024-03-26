package store.mybooks.resource.delivery_rule_name.enumulation;

import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.delivery_rule_name.enumulation<br>
 * fileName       : DeliveryRuleNameEnum<br>
 * author         : minsu11<br>
 * date           : 3/26/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/26/24        minsu11       최초 생성<br>
 */
@Getter
public enum DeliveryRuleNameEnum {
    DELIVERY_FEE("배송 비");

    private final String value;

    DeliveryRuleNameEnum(String value) {
        this.value = value;
    }

}
