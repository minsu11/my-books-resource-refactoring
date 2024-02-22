package store.mybooks.resource.category.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

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
    private Integer parentCategoryId;

    @NotBlank
    @Length(min = 1, max = 20)
    private String name;
}
