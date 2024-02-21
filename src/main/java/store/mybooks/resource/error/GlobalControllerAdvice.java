package store.mybooks.resource.error;

import javax.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import store.mybooks.resource.category.exception.CategoryNameAlreadyExistsException;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
import store.mybooks.resource.publisher.exception.PublisherAlreadyExistException;
import store.mybooks.resource.publisher.exception.PublisherNotExistException;
import store.mybooks.resource.tag.exception.TagNameAlreadyExistsException;
import store.mybooks.resource.tag.exception.TagNotExistsException;

/**
 * packageName    : store.mybooks.resource.error
 * fileName       : GlobalControllerAdvice
 * author         : damho-lee
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24          damho-lee          최초 생성
 */
@RestControllerAdvice
public class GlobalControllerAdvice {
    /**
     * methodName : xxxNotExistsException
     * author : damho-lee
     * description : NotFoundException 을 처리하는 ExceptionHandler.
     *
     * @param exception exception.
     * @return response entity
     */
    @ExceptionHandler({CategoryNotExistsException.class, TagNotExistsException.class, PublisherNotExistException.class})
    public ResponseEntity<String> xxxNotExistsException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    /**
     * methodName : xxxAlreadyExistsException
     * author : damho-lee
     * description : AlreadyExistsException 을 처리하는 ExceptionHandler.
     *
     * @param exception exception.
     * @return response entity
     */
    @ExceptionHandler({CategoryNameAlreadyExistsException.class, TagNameAlreadyExistsException.class,
            PublisherAlreadyExistException.class})
    public ResponseEntity<String> xxxAlreadyExistsException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<String> validationException(ValidationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
