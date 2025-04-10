package store.mybooks.resource.error.exception;

import java.util.stream.Collectors;
import jakarta.validation.ValidationException;
import org.springframework.validation.BindingResult;

/**
 * packageName    : store.mybooks.resource.error.exception
 * fileName       : ValidationFailException
 * author         : damho-lee
 * date           : 2/29/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/29/24          damho-lee          최초 생성
 */
public class ValidationFailException extends ValidationException {
    /**
     * ValidationFailException 생성자.
     *
     * @param bindingResult BindingResult
     */
    public ValidationFailException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors()
                .stream()
                .map(objectError -> String.format("objectName : %s, message: %s, error code: %s",
                        objectError.getObjectName(), objectError.getDefaultMessage(), objectError.getCode()))
                .collect(Collectors.joining("|")));
    }
}
