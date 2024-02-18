package store.mybooks.resource.tag.exception;

/**
 * packageName    : store.mybooks.resource.tag.exception
 * fileName       : TagNameAlreadyExistsException
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
public class TagNameAlreadyExistsException extends RuntimeException {
    public TagNameAlreadyExistsException(String name) {
        super("TagName : " + name + "이 이미 존재합니다.");
    }
}
