package store.mybooks.resource.user_grade_name.exception;

/**
 * packageName    : store.mybooks.resource.user_grade_name.exception
 * fileName       : UserGradeNameNotExistException
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public class UserGradeNameNotExistException extends RuntimeException {
    public UserGradeNameNotExistException(String userGradeName) {
        super(String.format("[%s]는 사용 불가능합니다 상태를 확인해주세요", userGradeName));
    }
}
