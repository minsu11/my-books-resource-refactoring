package store.mybooks.resource.eureka.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * packageName    : store.mybooks.resource.eureka.actuator
 * fileName       : CustomHealthIndicator
 * author         : minsu11
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        minsu11       최초 생성
 */
public class CustomHealthIndicator implements HealthIndicator {
    private final ApplicationStatus applicationStatus;

    public CustomHealthIndicator(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @Override
    public Health health() {
        if (!applicationStatus.getStatus()) {
            return Health.down().build();
        }
        return Health.up().withDetail("service", "start").build();
    }
}
