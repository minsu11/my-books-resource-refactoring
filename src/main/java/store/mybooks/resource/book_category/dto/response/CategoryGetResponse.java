package store.mybooks.resource.book_category.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.book_category.dto.response
 * fileName       : CategoryGetResponse
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
@Getter
@Setter
public class CategoryGetResponse {
    private Integer parentCategoryId;
    private String name;
}
