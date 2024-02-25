package store.mybooks.resource.book.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
    private Long id;
    @NotBlank
    @Size(min = 1, max = 20)
    private String bookStatusId;
    @NotBlank
    @Size(min = 1, max = 100)
    private Integer publisherId;
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    @NotBlank
    @Size(min = 13, max = 13)
    private String isbn;
    @NotBlank
    @Past
    private LocalDate publishDate;
    @NotBlank
    @Positive
    private Integer page;
    @NotBlank
    private String index;
    @NotBlank
    private String content;
    @NotBlank
    @Positive
    private Integer originalCost;
    @NotBlank
    @Positive
    private Integer saleCost;
    @NotBlank
    @Positive
    private Integer discountRate;
    @NotBlank
    @Positive
    private Integer stock;
    @NotBlank
    private Boolean isPacking;
}
