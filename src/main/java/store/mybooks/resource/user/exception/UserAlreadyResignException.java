package store.mybooks.resource.user.exception;

/**
 * packageName    : store.mybooks.resource.user.exception<br>
 * fileName       : UserAlreadyRegisnException<br>
 * author         : masiljangajji<br>
 * date           : 2/25/24<br>
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
