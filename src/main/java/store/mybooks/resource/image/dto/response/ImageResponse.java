package store.mybooks.resource.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.image.dto.response <br/>
 * fileName       : ImageResponse<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/12/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/12/24        Fiat_lux       최초 생성<br/>
 */
@Getter
@AllArgsConstructor
public class ImageResponse {
    private final String path;
    private final String fileName;
    private final String extension;
}
