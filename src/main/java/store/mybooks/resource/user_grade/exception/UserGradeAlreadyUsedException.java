package store.mybooks.resource.user_grade.exception;

/**
 * packageName    : store.mybooks.resource.user_grade.exception
 * fileName       : UserGradeNotAvailableException
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public class UserGradeAlreadyUsedException extends RuntimeException {

    public UserGradeAlreadyUsedException(String userGradeName) {
        super(String.format("[%s]는 이미 사용중입니다", userGradeName));
    }
}
