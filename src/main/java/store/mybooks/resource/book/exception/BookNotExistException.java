package store.mybooks.resource.book.exception;

/**
 * packageName    : store.mybooks.resource.book.exception <br/>
 * fileName       : BookNotExistException<br/>
 * author         : newjaehun <br/>
 * date           : 2/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/25/24        newjaehun       최초 생성<br/>
 */
public class BookNotExistException extends RuntimeException {
    public BookNotExistException(Long bookId) {
        super("도서ID " + bookId + "는 존재하지 않는 도서 ID 입니다");
    }
}
