package store.mybooks.resource.publisher.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import store.mybooks.resource.publisher.exception.PublisherAlreadyExistException;
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
    public ResponseEntity<String> notExistException(PublisherNotExistException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
    @ExceptionHandler(PublisherAlreadyExistException.class)
    public ResponseEntity<String> alreadyExistException(PublisherAlreadyExistException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
