package store.mybooks.resource.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.category.dto.response
 * fileName       : CategoryIdNameGetResponse
 * author         : damho-lee
 * date           : 2/24/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/24/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class CategoryIdNameGetResponse {
    private Integer id;
    private String name;
}
