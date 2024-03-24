package store.mybooks.resource.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.utils.TimeUtils;

/**
 * packageName    : store.mybooks.resource.user.entity<br>
 * fileName       : User<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
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

    @Column(name = "user_birth_year")
    private Integer birthYear;

    @Column(name = "user_birth_month_day")
    private String birthMonthDay;


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

    @Column(name = "user_oauth_id")
    private String oauthId;


    public User(String email, LocalDate birth, String password, String phoneNumber, Boolean isAdmin, String name,
                UserStatus userStatus,
                UserGrade userGrade, String oauthId) {
        this.email = email;

        this.birthYear = birth.getYear();
        this.birthMonthDay = String.format("%02d-%02d", birth.getMonthValue(), birth.getDayOfMonth());
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
        this.name = name;
        this.userStatus = userStatus;
        this.userGrade = userGrade;
        this.createdAt = TimeUtils.nowDateTime();
        this.gradeChangedDate = null;
        this.deletedAt = null;
        this.latestLogin = null;
        this.oauthId = oauthId;
    }

    public User(String email, Integer birthYear, String birthMonthDay, String password, String phoneNumber,
                Boolean isAdmin, String name, UserStatus userStatus, UserGrade userGrade, String oauthId) {

        this.email = email;
        this.birthYear = birthYear;
        this.birthMonthDay = birthMonthDay.replaceAll("(\\d{2})(\\d{2})", "$1-$2");
        this.password = password;
        this.phoneNumber = phoneNumber.replaceFirst("8210", "010");
        this.isAdmin = isAdmin;
        this.name = name;
        this.userStatus = userStatus;
        this.userGrade = userGrade;
        this.createdAt = TimeUtils.nowDateTime();
        this.gradeChangedDate = null;
        this.deletedAt = null;
        this.latestLogin = null;
        this.oauthId = oauthId;
    }


    public void modifyUser(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void modifyUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void modifyUserGrade(UserGrade userGrade) {
        this.userGrade = userGrade;
        this.gradeChangedDate = TimeUtils.nowDate();
    }

    public void modifyLatestLogin() {
        this.latestLogin = TimeUtils.nowDateTime();
    }

    public void modifyByDeleteRequest(UserStatus userStatus) {
        this.userStatus = userStatus;
        this.deletedAt = TimeUtils.nowDateTime();
    }

    public void modifyPassword(String password) {
        this.password = password;
    }


}
