package store.mybooks.resource.user_grade_name.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user_grade_name.entity<br>
 * fileName       : UserGradeName<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "user_grade_name")
public class UserGradeName {


    @Id
    @Column(name = "user_grade_name_id")
    String id;

}
