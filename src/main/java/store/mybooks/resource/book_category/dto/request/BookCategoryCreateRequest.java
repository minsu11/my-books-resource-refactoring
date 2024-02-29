package store.mybooks.resource.book_category.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.book_category.dto.request
 * fileName       : BookCategoryCreateRequest
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCategoryCreateRequest {
    @NotNull
    @Positive
    private Long bookId;
    @NotNull
    private List<Integer> categoryIdList;
}
