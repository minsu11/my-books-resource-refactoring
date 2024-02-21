package store.mybooks.resource.author.exception;

/**
 * packageName    : store.mybooks.resource.author.exception
 * fileName       : AuthorNotExistException
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
public class AuthorNotExistException extends RuntimeException{
    public AuthorNotExistException(Integer authorId) {
        super("저자ID " + authorId + "는 존재하지 않는 저자 ID 입니다");
    }
}
