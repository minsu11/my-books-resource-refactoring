package store.mybooks.resource.eureka.actuator;

import org.springframework.stereotype.Component;

/**
 * packageName    : store.mybooks.resource.eureka.actuator
 * fileName       : ApplicationStatus
 * author         : minsu11
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        minsu11       최초 생성
 */
@Component
public final class ApplicationStatus {
    private boolean status = true;

    public void stopService() {
        this.status = false;
    }

    public boolean getStatus() {
        return status;
    }
}