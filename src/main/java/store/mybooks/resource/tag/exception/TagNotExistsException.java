package store.mybooks.resource.tag.exception;

/**
 * packageName    : store.mybooks.resource.tag.exception
 * fileName       : TagNotExistsException
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
public class TagNotExistsException extends RuntimeException {
    public TagNotExistsException(int id) {
        super("TagId : " + id + "에 해당하는 태그가 존재하지 않습니다.");
    }
}
