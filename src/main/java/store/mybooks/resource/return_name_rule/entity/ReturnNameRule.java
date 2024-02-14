package store.mybooks.resource.return_name_rule.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.return_name_rule.entity
 * fileName       : ReturnNameRule
 * author         : minsu11
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        minsu11       최초 생성
 */

@Getter
@Setter
@Entity
@Table(name = "return_name_rule")
@NoArgsConstructor
@AllArgsConstructor
public class ReturnNameRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_name_rule_id")
    private Integer id;

    @Column(name = "return_name", nullable = false)
    private String name;

    @Column(name = "return_name_created_at", nullable = false)
    private LocalDate createdAt;

}
