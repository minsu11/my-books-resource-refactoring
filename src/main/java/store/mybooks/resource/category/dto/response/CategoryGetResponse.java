package store.mybooks.resource.category.dto.response;

import java.time.LocalDate;

/**
 * packageName    : store.mybooks.resource.category.dto.response
 * fileName       : CategoryGetResponseDto
 * author         : damho
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */
public interface CategoryGetResponse {
    Integer getId();

    CategoryGetResponse getParentCategory();

    String getName();

    LocalDate getCreatedDate();
}
