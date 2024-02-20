package store.mybooks.resource.cart.exception;

/**
 * packageName    : store.mybooks.resource.cart.exception
 * fileName       : CartAlreadyExistException
 * author         : Fiat_lux
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        Fiat_lux       최초 생성
 */
public class CartAlreadyExistException extends RuntimeException{
    public CartAlreadyExistException(String message) {
        super(message);
    }
}
