package store.mybooks.resource.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.category.dto.response
 * fileName       : CategoryGetResponseForBookCreate
 * author         : damho-lee
 * date           : 2/28/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/28/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class CategoryGetResponseForBookCreate {
    private Integer id;
    private String name;
}
