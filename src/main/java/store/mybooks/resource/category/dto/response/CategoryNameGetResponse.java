package store.mybooks.resource.category.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.category.dto.response
 * fileName       : CategoryNameGetResponse
 * author         : damho-lee
 * date           : 3/3/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/3/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class CategoryNameGetResponse {
    private List<String> names;
}
