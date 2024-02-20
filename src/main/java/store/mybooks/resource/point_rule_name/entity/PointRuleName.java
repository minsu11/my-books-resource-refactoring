package store.mybooks.resource.point_rule_name.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * packageName    : store.mybooks.resource.point_name_rule.entity
 * fileName       : PointNameRule
 * author         : damho
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        damho       최초 생성
 */
@Entity
@Table(name = "point_rule_name")
public class PointRuleName {
    @Id
    @Column(name = "point_rule_name_id")
    private String id;

    @Column(name = "point_rule_name_created_date")
    private LocalDate createdDate;
}
