package store.mybooks.resource.bookorder.exception;

/**
 * packageName    : store.mybooks.resource.book_order.exception<br>
 * fileName       : BookOrderNotExistException<br>
 * author         : minsu11<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/2/24        minsu11       최초 생성<br>
 */
public class BookOrderNotExistException extends RuntimeException {
    public BookOrderNotExistException() {
        super("주문이 존재 하지 않음");
    }
}
