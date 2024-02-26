package store.mybooks.resource.user.exception;

/**
 * packageName    : store.mybooks.resource.user.exception
 * fileName       : UserLoginFailException
 * author         : masiljangajji
 * date           : 2/25/24
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
