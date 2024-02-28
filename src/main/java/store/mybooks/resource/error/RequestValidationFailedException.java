package store.mybooks.resource.error;

import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

/**
 * packageName    : store.mybooks.resource.error <br/>
 * fileName       : RequestValidationFailedException<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/28/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/28/24        Fiat_lux       최초 생성<br/>
 */
public class RequestValidationFailedException extends RuntimeException {
    public RequestValidationFailedException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors()
                .stream()
                .map(objectError -> new StringBuilder()
                        .append("object: ")
                        .append(objectError.getObjectName())
                        .append(", message: ")
                        .append(objectError.getDefaultMessage())
                        .append(", error code: ")
                        .append(objectError.getCode()))
                .collect(Collectors.joining("|")));

    }
}
