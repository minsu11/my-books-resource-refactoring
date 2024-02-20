package store.mybooks.resource.point_rule.entity;

import java.time.LocalDate;
import javax.persistence.*;
import store.mybooks.resource.point_rule_name.entity.PointRuleName;

/**
 * packageName    : store.mybooks.resource.point_rule.entity
 * fileName       : PointRule
 * author         : damho
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        damho       최초 생성
 */
@Entity
@Table(name = "point_rule")
public class PointRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_rule_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_rule_name_id")
    private PointRuleName pointNameRule;

    @Column(name = "point_rule_rate")
    private Integer rate;

    @Column(name = "point_rule_cost")
    private Integer cost;

    @Column(name = "point_rule_created_date")
    private LocalDate createdDate;
}
