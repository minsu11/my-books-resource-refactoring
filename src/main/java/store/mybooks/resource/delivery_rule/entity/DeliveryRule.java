package store.mybooks.resource.delivery_rule.entity;

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
import lombok.Builder;
import lombok.NoArgsConstructor;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;

/**
 * packageName    : store.mybooks.resource.delivery_rule.entity
 * fileName       : DeliveryRule
 * author         : Fiat_lux
 * date           : 2/14/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/14/24        Fiat_lux       최초 생성
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "delivery_rule")
public class DeliveryRule {

    @Id
    @Column(name = "delivery_rule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "delivery_name_rule_id")
    private DeliveryNameRule deliveryNameRule;

    @Column(name = "delivery_company_name")
    private String companyName;

    @Column(name = "delivery_cost")
    private Integer cost;

    @Column(name = "delivery_rule_cost")
    private Integer ruleCost;

    @Column(name = "delivery_rule_created_at")
    private LocalDate createdAt;

    @Column(name = "is_available")
    private Boolean isAvailable;
}
