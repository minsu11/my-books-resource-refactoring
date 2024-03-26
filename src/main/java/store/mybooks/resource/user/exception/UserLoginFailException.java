package store.mybooks.resource.user.exception;

/**
 * packageName    : store.mybooks.resource.user.exception<br>
 * fileName       : UserLoginFailException<br>
 * author         : masiljangajji<br>
 * date           : 2/25/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/25/24        masiljangajji       최초 생성
 */
public class UserLoginFailException extends RuntimeException {
    public UserLoginFailException() {
        super("이메일을 확인해 주세요");
    }
}
