package store.mybooks.resource.category.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.category.dto.response
 * fileName       : CategoryGetResponseForMainView
 * author         : damho-lee
 * date           : 3/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/13/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class CategoryGetResponseForMainView {
    private Integer parentCategoryId;
    private String parentCategoryName;
    private List<CategoryIdNameGetResponse> childCategoryList;
}
