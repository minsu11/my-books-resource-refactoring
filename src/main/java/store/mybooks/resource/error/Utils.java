package store.mybooks.resource.error;

import org.springframework.validation.BindingResult;

/**
 * packageName    : store.mybooks.resource.error
 * fileName       : ValidationUtils
 * author         : damho-lee
 * date           : 3/1/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/24          damho-lee          최초 생성
 */
public class Utils {
    private Utils() {}

    public static void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationFailedException(bindingResult);
        }
    }
}
