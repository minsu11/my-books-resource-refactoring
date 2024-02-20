package store.mybooks.resource.user.exception;

/**
 * packageName    : store.mybooks.resource.user.exception
 * fileName       : UserNotExistException
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
public class UserNotExistException extends RuntimeException {


    public UserNotExistException(Long id) {
        super(String.format("[%d]번째 유저는 존재하지 않습니다", id));
    }

}
