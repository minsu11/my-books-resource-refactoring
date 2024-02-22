package store.mybooks.resource.return_rule.exception;

import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

/**
 * packageName    : store.mybooks.resource.return_rule.exception<br>
 * fileName       : ReturnRuleValidationFailedException<br>
 * author         : minsu11<br>
 * date           : 2/22/24<br>
 * description    : 반품 규정 요청이 유효성 검사에 실패한 경우의 {@code exception}
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/22/24        minsu11       최초 생성<br>
 */
public class ReturnRuleValidationFailedException extends RuntimeException {

    public ReturnRuleValidationFailedException(BindingResult bindingResult) {
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
