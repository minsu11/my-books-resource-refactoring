package store.mybooks.resource.pointrule.entity;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.pointrulename.entity.PointRuleName;

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
@Getter
@Entity
@Table(name = "point_rule")
@NoArgsConstructor
@AllArgsConstructor
public class PointRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_rule_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_rule_name_id")
    private PointRuleName pointRuleName;

    @Column(name = "point_rule_rate")
    private Integer rate;

    @Column(name = "point_rule_cost")
    private Integer cost;

    @Column(name = "point_rule_created_date")
    private LocalDate createdDate;

    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 생성자.
     *
     * @param pointRuleName the point rule name
     * @param rate          the rate
     * @param cost          the cost
     */
    public PointRule(PointRuleName pointRuleName, Integer rate, Integer cost) {
        this.pointRuleName = pointRuleName;
        this.rate = rate;
        this.cost = cost;
        this.createdDate = LocalDate.now();
        this.isAvailable = true;
    }

    public void modifyPointRuleIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;

    }


}
