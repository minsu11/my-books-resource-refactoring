package store.mybooks.resource.user_status.exception;

/**
 * packageName    : store.mybooks.resource.user_status.exception
 * fileName       : UserStatusAlreadyExistException
 * author         : masiljangajji
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        masiljangajji       최초 생성
 */
public class UserStatusAlreadyExistException extends RuntimeException{

    public UserStatusAlreadyExistException(String userStatusName) {
        super(String.format("[%s]는 이미 존재하는 User Status 입니다",userStatusName));
    }
}
