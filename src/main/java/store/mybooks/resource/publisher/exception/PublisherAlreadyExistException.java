package store.mybooks.resource.publisher.exception;

/**
 * packageName    : store.mybooks.resource.publisher.exception
 * fileName       : PublisherAlreadyExistException
 * author         : newjaehun
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        newjaehun       최초 생성
 */
public class PublisherAlreadyExistException extends RuntimeException {
    public PublisherAlreadyExistException(String publisherName) {
        super("출판사명 " +  publisherName + "는 이미 존재하는 출판사입니다");
    }
}
