package store.mybooks.resource.book.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class BookModifyRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String bookStatusId;
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
