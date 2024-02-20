package store.mybooks.resource.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * packageName    : store.mybooks.resource.config
 * fileName       : DatabaseProperties
 * author         : Fiat_lux
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        Fiat_lux       최초 생성
 */
@ConfigurationProperties(prefix = "database.mysql")
@Getter
@Setter
public class DatabaseProperties {
    private String url;
    private String userName;
    private String password;
    private String initialSize;
    private Integer maxTotal;
    private Integer minIdle;
    private Integer maxIdle;
    private Integer maxWait;
}
