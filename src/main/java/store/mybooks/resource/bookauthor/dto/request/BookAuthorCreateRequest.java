package store.mybooks.resource.bookauthor.dto.request;

import java.util.List;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.book_author.dto.request <br/>
 * fileName       : BookAuthorCreateRequest<br/>
 * author         : newjaehun <br/>
 * date           : 3/1/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/1/24        newjaehun       최초 생성<br/>
 */
@Getter
@AllArgsConstructor
public class BookAuthorCreateRequest {
    @NotNull
    @Positive
    private Long bookId;
    @NotNull
    @Size(min = 1)
    private List<Integer> authorIdList;
}
