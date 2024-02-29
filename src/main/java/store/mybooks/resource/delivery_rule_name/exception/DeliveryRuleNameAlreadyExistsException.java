package store.mybooks.resource.delivery_rule_name.exception;

/**
 * packageName    : store.mybooks.resource.delivery_rule_name.exception <br/>
 * fileName       : DeliveryRuleNameAlreadyExistsException<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/28/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/28/24        Fiat_lux       최초 생성<br/>
 */
public class DeliveryRuleNameAlreadyExistsException extends RuntimeException {
    public DeliveryRuleNameAlreadyExistsException(String message) {
        super(message);
    }
}
