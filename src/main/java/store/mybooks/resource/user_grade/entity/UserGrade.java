package store.mybooks.resource.user_grade.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.utils.TimeUtils;

/**
 * packageName    : store.mybooks.resource.user.entity<br>
 * fileName       : UserGrade<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "user_grade")
public class UserGrade {

    @Id
    @Column(name = "user_grade_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_grade_name_id")
    private UserGradeName userGradeName;


    @Column(name = "user_grade_min")
    private Integer minCost;

    @Column(name = "user_grade_max")
    private Integer maxCost;


    @Column(name = "user_grade_rate")
    private Integer rate;

    @Column(name = "user_grade_created_date")
    private LocalDate createdDate;

    @Column(name = "is_available")
    private Boolean isAvailable;


    public UserGrade(Integer minCost, Integer maxCost, Integer rate,
                     UserGradeName userGradeName) {
        this.userGradeName = userGradeName;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.rate = rate;
        this.createdDate = TimeUtils.nowDate();
        this.isAvailable = true;
    }

    public void deleteUserGrade() {
        this.isAvailable = false;
    }


}
