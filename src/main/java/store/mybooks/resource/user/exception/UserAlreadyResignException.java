package store.mybooks.resource.user.exception;

/**
 * packageName    : store.mybooks.resource.user.exception
 * fileName       : UserAlreadyRegisnException
 * author         : masiljangajji
 * date           : 2/25/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/25/24        masiljangajji       최초 생성
 */
public class UserAlreadyResignException extends RuntimeException{
    public UserAlreadyResignException() {
        super("탈퇴한 회원입니다!");
    }
}
