package store.mybooks.resource.category.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.category.dto.response
 * fileName       : CategoryGetResponseForCategoryView
 * author         : damho-lee
 * date           : 3/14/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/14/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class CategoryGetResponseForCategoryView {
    private String highestCategoryName;
    private String name;
    private List<CategoryIdNameGetResponse> levelTwoCategories;
    private List<CategoryIdNameGetResponse> targetCategories;
}
