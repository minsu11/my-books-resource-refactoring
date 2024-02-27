package store.mybooks.resource.image.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.image.dto <br/>
 * fileName       : TokenRequest<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/25/24        Fiat_lux       최초 생성<br/>
 */
@Data
@NoArgsConstructor
public class TokenRequest {
    private Auth auth;

    @Data
    @AllArgsConstructor
    public static class Auth {
        private String tenantId;
        private PasswordCredentials passwordCredentials;
    }

    @Data
    @AllArgsConstructor
    public static class PasswordCredentials {
        private String username;
        private String password;
    }

    public TokenRequest(String tenantId, String username, String password) {
        this.auth = new Auth(tenantId, new PasswordCredentials(username, password));
    }
}
