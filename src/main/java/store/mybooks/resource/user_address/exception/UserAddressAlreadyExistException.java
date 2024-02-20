package store.mybooks.resource.user_address.exception;

/**
 * packageName    : store.mybooks.resource.user_address.exception
 * fileName       : UserAddressAlreadyExistException
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public class UserAddressAlreadyExistException extends RuntimeException{
    public UserAddressAlreadyExistException(String alias) {
        super(String.format("[%s]는 이미 존재하는 주소별명입니다",alias));
    }
}
