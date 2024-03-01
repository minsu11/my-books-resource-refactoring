package store.mybooks.resource.book_author.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorCreateRequest {
    @NotNull
    @Positive
    private Long bookId;
    @NotNull
    private List<Integer> authorIdList;
}
