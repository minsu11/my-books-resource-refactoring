package store.mybooks.resource.pointhistory.exception;

/**
 * packageName    : store.mybooks.resource.pointhistory.exception
 * fileName       : AlreadyReceivedSignUpPoint
 * author         : damho-lee
 * date           : 3/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/20/24          damho-lee          최초 생성
 */
public class AlreadyReceivedSignUpPoint extends RuntimeException {
    public AlreadyReceivedSignUpPoint() {
        super("회원가입 포인트가 이미 적립된 적이 있습니다.");
    }
}