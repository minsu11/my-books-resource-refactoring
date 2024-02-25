package store.mybooks.resource.book_status.exception;

/**
 * packageName    : store.mybooks.resource.book_status.exception <br/>
 * fileName       : BookStatusNotExistException<br/>
 * author         : newjaehun <br/>
 * date           : 2/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/25/24        newjaehun       최초 생성<br/>
 */
public class BookStatusNotExistException extends RuntimeException {
    public BookStatusNotExistException(String bookStatusId) {
        super("도사상태 " + bookStatusId + "는 존재하지 않습니다");
    }
}
