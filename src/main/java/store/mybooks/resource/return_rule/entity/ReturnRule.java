package store.mybooks.resource.return_rule.entity;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.return_rule_name.entity.ReturnRuleName;

/**
 * packageName    : store.mybooks.resource.return_rule.entity
 * fileName       : ReturnRule
 * author         : minsu
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        minsu       최초 생성
 */
@Getter
@Entity
@Table(name = "return_rule")
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_rule_id")
    private Integer id;

    @Column(name = "return_rule_delivery_fee")
    private Integer deliveryFee;

    @Column(name = "return_rule_term")
    private Integer term;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "return_created_date")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_rule_name_id")
    private ReturnRuleName returnNameRule;

}
