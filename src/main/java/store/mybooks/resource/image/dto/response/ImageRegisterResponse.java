package store.mybooks.resource.image.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.image.dto <br/>
 * fileName       : ImageRegisterResponse<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        Fiat_lux       최초 생성<br/>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageRegisterResponse {
    private String path;
    private String fileName;
    private String extension;
    private LocalDate createdDate;
}
