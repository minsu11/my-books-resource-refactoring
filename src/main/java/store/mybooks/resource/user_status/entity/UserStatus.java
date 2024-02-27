package store.mybooks.resource.user_status.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user.entity<br>
 * fileName       : UserStatus<br>
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
@Table(name = "user_status")
public class UserStatus {

    @Id
    @Column(name = "user_status_id")
    private String id;


}
