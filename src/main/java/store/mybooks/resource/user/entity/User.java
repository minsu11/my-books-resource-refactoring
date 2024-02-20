package store.mybooks.resource.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Bag;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_status.entity.UserStatus;

/**
 * packageName    : store.mybooks.resource.user.entity
 * fileName       : User
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_grade_id")
    private UserGrade userGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_status_id")
    private UserStatus userStatus;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_phone_number")
    private String phoneNumber;

    @Column(name = "user_email")
    private String email;


    @Column(name = "user_birth")
    private LocalDate birth;


    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "user_created_at")
    private LocalDateTime createdAt;


    @Column(name = "user_latest_login_at")
    private LocalDateTime latestLogin;

    @Column(name = "user_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "user_grade_changed_date")
    private LocalDate gradeChangedDate;


    public User(UserCreateRequest createRequest, UserStatus userStatus, UserGrade userGrade) {
        this.email = createRequest.getEmail();
        this.birth = createRequest.getBirth();
        this.password = createRequest.getPassword();
        this.phoneNumber = createRequest.getPhoneNumber();
        this.isAdmin = createRequest.getIsAdmin();
        this.name = createRequest.getName();
        this.userStatus = userStatus;
        this.userGrade = userGrade;

        this.createdAt = LocalDateTime.now();
        this.gradeChangedDate = null;
        this.deletedAt = null;
        this.latestLogin = null;
    }


    public void setByModifyRequest(UserModifyRequest modifyRequest, UserStatus userStatus,
                                   UserGrade userGrade) {

        this.name = modifyRequest.getName();
        this.password = modifyRequest.getPassword();
        this.latestLogin = modifyRequest.getLatestLogin();
        this.deletedAt = modifyRequest.getDeletedAt();
        this.gradeChangedDate = modifyRequest.getGradeChangeDate();
        this.phoneNumber = modifyRequest.getPhoneNumber();

        this.userGrade = userGrade;
        this.userStatus = userStatus;
    }

    public void modifyUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }


}
