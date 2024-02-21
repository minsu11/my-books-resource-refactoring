package store.mybooks.resource.category.exception;

/**
 * packageName    : store.mybooks.resource.category.exception
 * fileName       : CategoryNotExistsException
 * author         : damho-lee
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */
public class CategoryNotExistsException extends RuntimeException {
    public CategoryNotExistsException(int id) {
        super("CategoryId : " + id + "에 해당하는 카테고리가 존재하지 않습니다.");
    }
}
