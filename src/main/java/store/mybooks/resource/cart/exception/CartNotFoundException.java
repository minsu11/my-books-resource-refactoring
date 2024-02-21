package store.mybooks.resource.cart.exception;

/**
 * packageName    : store.mybooks.resource.cart.exception
 * fileName       : CartNotFoundException
 * author         : Fiat_lux
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        Fiat_lux       최초 생성
 */
public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
