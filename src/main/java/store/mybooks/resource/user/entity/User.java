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
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_status_id")
    private UserGrade userGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_grade_id")
    private UserStatus userStatus;

    @Column(name = "user_name")
    private String name;


    @Column(name = "user_password")
    private String password;

    @Column(name = "user_phonenumber")
    private String phoneNumber;

    @Column(name = "user_email")
    private String email;


    @Column(name = "user_birth")
    private LocalDate birth;


    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "user_point")
    private Integer point;

    @Column(name = "user_created_at")
    private LocalDateTime createdAt;


    @Column(name = "user_lastest_login")
    private LocalDateTime lastestLogin;

    @Column(name = "user_delete_at")
    private LocalDateTime deleteAt;


}
