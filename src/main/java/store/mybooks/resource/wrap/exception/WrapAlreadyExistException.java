package store.mybooks.resource.wrap.exception;

/**
 * packageName    : store.mybooks.resource.wrap.exception<br>
 * fileName       : WrapAlreadyExistException<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */

public class WrapAlreadyExistException extends RuntimeException {
    public WrapAlreadyExistException() {
        super("이미 존재하는 포장지가 있음.");
    }
}
