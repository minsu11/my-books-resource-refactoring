package store.mybooks.resource.wrap.exception;

import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

/**
 * packageName    : store.mybooks.resource.wrap.exception<br>
 * fileName       : WrapValidationFailedException<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
public class WrapValidationFailedException extends RuntimeException {
    public WrapValidationFailedException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors()
                .stream()
                .map(objectError -> new StringBuilder().append("name: ")
                        .append(objectError.getObjectName())
                        .append(", message: ")
                        .append(objectError.getDefaultMessage())
                        .append(", error code: ")
                        .append(objectError.getCode())).collect(Collectors.joining("|")));


    }
}
