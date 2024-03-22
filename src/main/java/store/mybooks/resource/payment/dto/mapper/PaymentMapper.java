package store.mybooks.resource.payment.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.dto.response.PayModifyResponse;
import store.mybooks.resource.payment.entity.Payment;

/**
 * packageName    : store.mybooks.resource.payment.dto.mapper<br>
 * fileName       : PaymentMapper<br>
 * author         : minsu11<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/19/24        minsu11       최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMapper {
    @Mapping(source = "id", target = "payId")
    @Mapping(source = "orderNumber", target = "paymentKey")
    @Mapping(source = "cost", target = "totalAmount")
    PayCreateResponse mapToPayCreateRequest(Payment payment);

    PayModifyResponse mapToPayModifyResponse(Payment payment);
}
