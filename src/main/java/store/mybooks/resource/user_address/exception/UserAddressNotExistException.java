package store.mybooks.resource.user_address.exception;

/**
 * packageName    : store.mybooks.resource.user_address.exception<br>
 * fileName       : UserAddressNotExistException<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public class UserAddressNotExistException extends RuntimeException {
    public UserAddressNotExistException(Long id) {
        super(String.format("[%d]번쨰 유저주소는 존재하지 않습니다.", id));
    }
}
