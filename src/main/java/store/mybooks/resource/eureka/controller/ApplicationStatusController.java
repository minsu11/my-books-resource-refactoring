package store.mybooks.resource.eureka.controller;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.eureka.actuator.ApplicationStatus;

/**
 * packageName    : store.mybooks.resource.eureka.controller
 * fileName       : ApplicationStatusController
 * author         : minsu11
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        minsu11       최초 생성
 */
@RestController
@RequestMapping("/api/actuator/status")
public class ApplicationStatusController {

    private final ApplicationInfoManager applicationInfoManager;
    private final ApplicationStatus applicationStatus;

    public ApplicationStatusController(ApplicationInfoManager applicationInfoManager,
                                       ApplicationStatus applicationStatus) {
        this.applicationInfoManager = applicationInfoManager;
        this.applicationStatus = applicationStatus;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void stopStatus() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
        applicationStatus.stopService();
    }

}