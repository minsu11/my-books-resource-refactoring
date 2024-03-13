package store.mybooks.resource.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * packageName    : store.mybooks.resource.config <br/>
 * fileName       : RedisProperties<br/>
 * author         : newjaehun <br/>
 * date           : 3/12/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/12/24        newjaehun       최초 생성<br/>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "my-books.redis")
@RequiredArgsConstructor
public class RedisProperties {
    private String host;
    private int port;
    private String password;
    private int database;
}
