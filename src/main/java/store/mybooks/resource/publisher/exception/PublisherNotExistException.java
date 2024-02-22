package store.mybooks.resource.publisher.exception;

/**
 * packageName    : store.mybooks.resource.publisher.exception
 * fileName       : PublisherNotExistException
 * author         : newjaehun
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        newjaehun       최초 생성
 */
public class PublisherNotExistException extends RuntimeException {
    public PublisherNotExistException(Integer publisherId) {
        super("출판사ID " + publisherId + "는 존재하지 않는 출판사 ID 입니다");
    }
}
