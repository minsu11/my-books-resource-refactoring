package store.mybooks.resource.author.exception;

/**
 * packageName    : store.mybooks.resource.author.exception
 * fileName       : AuthorAlreadyExistException
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
public class AuthorAlreadyExistException extends RuntimeException {
    public AuthorAlreadyExistException(String authorName) {
        super("저자명 " +  authorName + "는 이미 존재하는 저자입니다");
    }
}
