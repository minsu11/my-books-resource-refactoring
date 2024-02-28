package store.mybooks.resource.delivery_rule.exception;

/**
 * packageName    : store.mybooks.resource.delivery_rule.exception
 * fileName       : DeliveryRuleNotFoundException
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
public class DeliveryRuleNotExistsException extends RuntimeException {
    public DeliveryRuleNotExistsException(String message) {
        super(message);
    }
}
