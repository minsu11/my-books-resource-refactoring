package store.mybooks.resource.orders_status.exception;

/**
 * packageName    : store.mybooks.resource.orders_status.exception
 * fileName       : OrdersStatusNotFoundException
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
public class OrdersStatusNotFoundException extends RuntimeException {
    public OrdersStatusNotFoundException() {
    }

    public OrdersStatusNotFoundException(String message) {
        super(message);
    }
}
