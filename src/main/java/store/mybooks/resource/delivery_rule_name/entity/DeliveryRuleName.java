package store.mybooks.resource.delivery_rule_name.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule
 * fileName       : DeliveryNameRule
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
@Getter
@Entity
@Table(name = "delivery_rule_name")
public class DeliveryRuleName {

    @Id
    @Column(name = "delivery_rule_name_id")
    private String id;

    @Column(name = "delivery_rule_name_created_date")
    private LocalDate createdDate;

    public DeliveryRuleName(String id) {
        this.id = id;
        this.createdDate = LocalDate.now();
    }
}
