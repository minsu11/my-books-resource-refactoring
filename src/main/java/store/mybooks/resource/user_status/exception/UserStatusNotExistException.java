package store.mybooks.resource.user_status.exception;

/**
 * packageName    : store.mybooks.resource.user_status.exception<br>
 * fileName       : UserStatusNotExistException<br>
 * author         : masiljangajji<br>
 * date           : 2/16/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        masiljangajji       최초 생성
 */
public class UserStatusNotExistException extends RuntimeException{

    public UserStatusNotExistException(String userStatusName) {
        super(String.format("[%s]는 존재하지 않는 User Status 입니다",userStatusName));
    }
}
