package store.mybooks.resource.tag.exception;

import java.util.stream.Collectors;
import javax.validation.ValidationException;
import org.springframework.validation.BindingResult;

/**
 * packageName    : store.mybooks.resource.tag.exception
 * fileName       : TagValidationException
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
public class TagValidationException extends ValidationException {
    /**
     * TagValidationException 생성자.
     *
     * @param bindingResult the binding result
     */
    public TagValidationException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors()
                .stream()
                .map(objectError -> String.format("objectName : %s, message: %s, error code: %s",
                        objectError.getObjectName(), objectError.getDefaultMessage(), objectError.getCode()))
                .collect(Collectors.joining("|")));
    }
}