package store.mybooks.resource.book.exception;

/**
 * packageName    : store.mybooks.resource.book.exception <br/>
 * fileName       : IsbnAlreadyExistsException<br/>
 * author         : newjaehun <br/>
 * date           : 2/29/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/29/24        newjaehun       최초 생성<br/>
 */
public class IsbnAlreadyExistsException extends RuntimeException {
    public IsbnAlreadyExistsException(String isbn) {
        super(String.format("ISBN [%s]은 이미 존재합니다", isbn));
    }
}
