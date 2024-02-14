package store.mybooks.resource.return_rule.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.return_name_rule.entity.ReturnNameRule;

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

    @Column(name = "return_rule_delivery_fee", nullable = false)
    private Integer deliveryFee;

    @Column(name = "return_rule_term", nullable = false)
    private Integer term;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "return_created_at", nullable = false)
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "return_name_rule_id", nullable = false)
    private ReturnNameRule returnNameRule;

}
