package store.mybooks.resource.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "storage")
public class ObjectStorageProperties {
    private String url;
    private String username;
    private String containerName;
    private String tenantId;
    private String password;
    private String identity;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}