package store.mybooks.resource.error;

import javax.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import store.mybooks.resource.category.exception.CannotDeleteParentCategoryException;
import store.mybooks.resource.category.exception.CategoryNameAlreadyExistsException;
import store.mybooks.resource.category.exception.CategoryNotExistsException;
import store.mybooks.resource.delivery_rule.exception.DeliveryRuleNotExistsException;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameAlreadyExistsException;
import store.mybooks.resource.delivery_rule_name.exception.DeliveryRuleNameNotExistsException;
import store.mybooks.resource.publisher.exception.PublisherAlreadyExistException;
import store.mybooks.resource.publisher.exception.PublisherNotExistException;
import store.mybooks.resource.return_rule.exception.ReturnRuleValidationFailedException;
import store.mybooks.resource.return_rule_name.exception.ReturnRuleNameRequestValidationFailedException;
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
     * @return ResponseEntity
     */
    @ExceptionHandler({CategoryNotExistsException.class, TagNotExistsException.class, PublisherNotExistException.class,
            DeliveryRuleNameNotExistsException.class, DeliveryRuleNotExistsException.class})
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
     * @return ResponseEntity
     */
    @ExceptionHandler({CategoryNameAlreadyExistsException.class, TagNameAlreadyExistsException.class,
            PublisherAlreadyExistException.class, DeliveryRuleNameAlreadyExistsException.class})
    public ResponseEntity<String> xxxAlreadyExistsException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }


    /**
     * methodName : validationException <br>
     * author : damho-lee <br>
     * description : ValidationException 을 처리하는 ExceptionHandler. <br>
     *
     * @param exception ValidationException.
     * @return ResponseEntity
     */
    @ExceptionHandler({ValidationException.class, ReturnRuleNameRequestValidationFailedException.class, ReturnRuleValidationFailedException.class, RequestValidationFailedException.class})
    public ResponseEntity<String> validationException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    /**
     * methodName : cannotDeleteParentCategoryException <br>
     * author : damho-lee <br>
     * description : 자식 카테고리가 있는 카테고리를 삭제하려는 경우 발생하는 CannotDeleteParentCategoryException 을 처리하기 위한 ExceptionHandler.<br>
     *
     * @param exception CannotDeleteParentCategoryException
     * @return ResponseEntity
     */
    @ExceptionHandler(CannotDeleteParentCategoryException.class)
    public ResponseEntity<String> cannotDeleteParentCategoryException(CannotDeleteParentCategoryException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
