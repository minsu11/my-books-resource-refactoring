package store.mybooks.resource.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : store.mybooks.resource.config <br/>
 * fileName       : ObjectStorage<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/23/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/23/24        Fiat_lux       최초 생성<br/>
 */

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "storage")
public class ObjectStorageProperties {
    private KeyConfig keyConfig;
    private String url;
    private String username;
    private String containerName;
    private String tenantId;
    private String password;
    private String identity;
    private String book;
    private String review;
}