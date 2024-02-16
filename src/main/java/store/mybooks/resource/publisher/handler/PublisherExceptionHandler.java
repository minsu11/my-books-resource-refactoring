package store.mybooks.resource.publisher.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
import store.mybooks.resource.publisher.exception.PublisherNotExistException;

/**
 * packageName    : store.mybooks.resource.publisher.handler
 * fileName       : PublisherExceptionHandler
 * author         : newjaehun
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        newjaehun       최초 생성
 */
@ControllerAdvice(basePackages = "store.mybooks.resource.publisher")
public class PublisherExceptionHandler {

    @ExceptionHandler(PublisherNotExistException.class)
    public ResponseEntity<PublisherDeleteResponse> notExistException(){
        return new ResponseEntity<>(new PublisherDeleteResponse("존재하지 않는 출판사"), HttpStatus.NOT_FOUND);
    }
}
