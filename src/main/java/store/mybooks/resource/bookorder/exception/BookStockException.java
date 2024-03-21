package store.mybooks.resource.bookorder.exception;

/**
 * packageName    : store.mybooks.resource.bookorder.exception<br>
 * fileName       : BookStockException<br>
 * author         : minsu11<br>
 * date           : 3/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/20/24        minsu11       최초 생성<br>
 */
public class BookStockException extends RuntimeException {
    public BookStockException() {
        super("재고가 부족");
    }
}
