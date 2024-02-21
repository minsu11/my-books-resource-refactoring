package store.mybooks.resource.user_address.exception;

/**
 * packageName    : store.mybooks.resource.user_address.exception
 * fileName       : UserAddressFullException
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */
public class UserAddressFullException extends RuntimeException{

    public UserAddressFullException(Long userId) {
        super(String.format("[%d]번쨰 유저는 이미 10개 이상의 주소를 갖고있습니다",userId));
    }
}
