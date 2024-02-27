package store.mybooks.resource.category.exception;

/**
 * packageName    : store.mybooks.resource.category.exception
 * fileName       : CategoryDeleteFailException
 * author         : damho-lee
 * date           : 2/25/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/25/24          damho-lee          최초 생성
 */
public class CannotDeleteParentCategoryException extends RuntimeException {
    public CannotDeleteParentCategoryException() {
        super("자식 카테고리가 존재하기 때문에 삭제할 수 없습니다.");
    }
}
