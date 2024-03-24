package store.mybooks.resource.user.exception;

/**
 * packageName    : store.mybooks.resource.user.exception<br>
 * fileName       : UserAlreadyExistException<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String email) {
        super(String.format("[%s]는 사용할 수 없는 email입니다.", email));
    }
}
