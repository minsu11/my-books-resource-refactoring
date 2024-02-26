package store.mybooks.resource.user_grade.exception;

/**
 * packageName    : store.mybooks.resource.user_grade.exception
 * fileName       : UserGradeNotExistException
 * author         : masiljangajji
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        masiljangajji       최초 생성
 */
public class UserGradeIdNotExistException extends RuntimeException {
    public UserGradeIdNotExistException() {
        super("존재하지 않는 User Grade 입니다.");
    }
}
