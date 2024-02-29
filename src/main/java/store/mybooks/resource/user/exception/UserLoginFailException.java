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
public class UserLoginFailException extends RuntimeException{
    public UserLoginFailException() {
        super(String.format("로그인 실패"));
    }
}
