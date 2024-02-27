package store.mybooks.resource.user_address.exception;

/**
 * packageName    : store.mybooks.resource.user_address.exception<br>
 * fileName       : UserAddressAlreadyExistException<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
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
