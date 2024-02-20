package store.mybooks.resource.delivery_name_rule.exception;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.exception
 * fileName       : DeliveryNameRuleNotFoundException
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
public class DeliveryRuleNameNotFoundException extends RuntimeException {
    public DeliveryRuleNameNotFoundException(String message) {
        super(message);
    }
}
