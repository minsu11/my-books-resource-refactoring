package store.mybooks.resource.delivery_rule_name.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameDto;
import store.mybooks.resource.delivery_rule_name.dto.response.DeliveryRuleNameResponse;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.delivery_rule_name.entity.QDeliveryRuleName;

/**
 * packageName    : store.mybooks.resource.delivery_rule_name.repository <br/>
 * fileName       : DeliveryRuleNameRepositoryImpl<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/10/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/10/24        Fiat_lux       최초 생성<br/>
 */
public class DeliveryRuleNameRepositoryImpl extends QuerydslRepositorySupport
        implements DeliveryRuleNameRepositoryCustom {


    public DeliveryRuleNameRepositoryImpl() {
        super(DeliveryRuleName.class);
    }

    @Override
    public List<DeliveryRuleNameResponse> getDeliveryRuleNameList() {
        QDeliveryRuleName deliveryRuleName = QDeliveryRuleName.deliveryRuleName;

        return from(deliveryRuleName)
                .select(
                        Projections.constructor(
                                DeliveryRuleNameResponse.class,
                                deliveryRuleName.id,
                                deliveryRuleName.createdDate)
                )
                .fetch();
    }
}
