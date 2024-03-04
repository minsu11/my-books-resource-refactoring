package store.mybooks.resource.book.dto.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.book.dto.request <br/>
 * fileName       : BookModifyRequest<br/>
 * author         : newjaehun <br/>
 * date           : 2/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/25/24        newjaehun       최초 생성<br/>
 */
@Getter
@NoArgsConstructor
public class BookModifyRequest {
    @NotNull
    @Positive
    private Integer saleCost;
    @NotBlank
    @Size(min = 1, max = 20)
    private String bookStatusId;
    @NotNull
    @PositiveOrZero
    private Integer stock;
    @NotNull
    private Boolean isPacking;
    @NotNull
    private List<Integer> authorList;
    @NotNull
    private List<Integer> categoryList;

    private List<Integer> tagList;
}