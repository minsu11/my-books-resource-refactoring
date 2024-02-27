package store.mybooks.resource.image.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.image.dto <br/>
 * fileName       : TokenResponse<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        Fiat_lux       최초 생성<br/>
 */
@Getter
public class TokenResponse {
    private Access access;

    @Getter
    @NoArgsConstructor
    public static class Access{
        Token token;
    }

    @Getter
    @NoArgsConstructor
    public static class Token{
        LocalDateTime dateTime;
        String id;
    }
}
