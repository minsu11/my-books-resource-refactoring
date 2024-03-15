package store.mybooks.resource.image.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.image.dto <br/>
 * fileName       : ImageRegisterRequest<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        Fiat_lux       최초 생성<br/>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageRegisterRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String imageStatusId;
    private Long reviewId;
    private Long bookId;
}
