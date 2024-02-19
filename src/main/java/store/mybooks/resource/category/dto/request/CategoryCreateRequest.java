package store.mybooks.resource.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import store.mybooks.resource.category.entity.Category;

/**
 * packageName    : store.mybooks.resource.category.dto.request
 * fileName       : CategoryCreateRequest
 * author         : damho
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class CategoryCreateRequest {
    private Category parentCategory;
    private String name;
}
