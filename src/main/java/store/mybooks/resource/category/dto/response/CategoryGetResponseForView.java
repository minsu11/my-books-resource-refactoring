package store.mybooks.resource.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.category.dto.response
 * fileName       : CategoryGetResponseForView
 * author         : damho-lee
 * date           : 2/27/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/27/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class CategoryGetResponseForView {
    private Integer id;
    private String name;
    private String parentCategoryName;
}
