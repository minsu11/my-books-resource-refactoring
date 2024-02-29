package store.mybooks.resource.wrap.exception;

/**
 * packageName    : store.mybooks.resource.wrap<br>
 * fileName       : WrapNotExistException<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
public class WrapNotExistException extends RuntimeException {
    public WrapNotExistException() {
        super("포장지를 찾을 수 없음");
    }
}
