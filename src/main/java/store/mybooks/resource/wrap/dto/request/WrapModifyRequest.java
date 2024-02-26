package store.mybooks.resource.wrap.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.wrap.dto.request<br>
 * fileName       : WrapModifyReqeust<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class WrapModifyRequest {
    @Size(min = 2, max = 20)
    private String wrapName;

    @Positive
    @Max(100000)
    private Integer wrapCost;
    
    @NotBlank
    private Boolean isAvailable;
}
