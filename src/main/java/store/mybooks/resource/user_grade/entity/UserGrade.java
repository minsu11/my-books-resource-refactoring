package store.mybooks.resource.user_grade.entity;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user.entity
 * fileName       : UserGrade
 * author         : masiljangajji
 * date           : 2/13/24
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

    @Column(name = "user_grade_name")
    private String name;


    @Column(name = "user_grade_min")
    private Integer minCost;

    @Column(name = "user_grade_max")
    private Integer maxCost;


    @Column(name = "user_grade_rate")
    private Integer rate;

    @Column(name = "user_grade_created_date")
    private LocalDate createdDate;


}
