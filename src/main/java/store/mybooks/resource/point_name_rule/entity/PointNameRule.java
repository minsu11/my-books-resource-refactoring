package store.mybooks.resource.point_name_rule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * packageName    : store.mybooks.resource.domain.entity
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
@Table(name = "point_name_rule")
public class PointNameRule {
    @Id
    @Column(name = "point_name_rule_id")
    private int id;

    @Column(name = "point_name")
    private String pointName;
}
