package store.mybooks.resource.user_grade.entity;

import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;

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
    @NotNull
    private UserGradeName userGradeName;


    @Column(name = "user_grade_min")
    @NotNull
    @Positive
    private Integer minCost;

    @Column(name = "user_grade_max")
    @NotNull
    @Positive
    private Integer maxCost;


    @Column(name = "user_grade_rate")
    @NotNull
    @Positive
    private Integer rate;

    @Column(name = "user_grade_created_date")
    @NotNull
    private LocalDate createdDate;

    @Column(name = "is_available")
    @NotNull
    private Boolean isAvailable;


    public UserGrade(Integer minCost, Integer maxCost, Integer rate, LocalDate createdDate,
                     UserGradeName userGradeName) {
        this.userGradeName = userGradeName;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.rate = rate;
        this.createdDate = createdDate;
        this.isAvailable = true;
    }

    public void deleteUserGrade() {
        this.isAvailable = false;
    }


}
