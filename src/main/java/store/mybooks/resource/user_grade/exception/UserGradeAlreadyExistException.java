package store.mybooks.resource.user_grade.exception;

/**
 * packageName    : store.mybooks.resource.user_grade.exception
 * fileName       : UserGradeAlreadyExistException
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public class UserGradeAlreadyExistException extends RuntimeException {
    public UserGradeAlreadyExistException(String userGradeName) {
        super(String.format("[%s]는 이미 존재하는 userGradeName 입니다.", userGradeName));
    }
}
