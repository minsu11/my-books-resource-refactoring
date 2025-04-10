package store.mybooks.resource.book.dto.request;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.book.dto.request <br/>
 * fileName       : BookCreateRequest<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String bookStatusId;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer publisherId;
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    @NotBlank
    @Size(min = 13, max = 13)
    private String isbn;
    @NotNull
    @PastOrPresent
    private LocalDate publishDate;
    @NotNull
    @Positive
    private Integer page;
    @NotBlank
    private String index;
    @NotBlank
    private String explanation;
    @NotNull
    @Positive
    private Integer originalCost;
    @NotNull
    @Positive
    private Integer saleCost;
    @NotNull
    @PositiveOrZero
    private Integer stock;
    @NotNull
    private Boolean isPacking;

    @NotNull
    @Size(min = 1)
    private List<Integer> authorList;
    @NotNull
    @Size(min = 1, max = 10)
    private List<Integer> categoryList;

    private List<Integer> tagList;
}
