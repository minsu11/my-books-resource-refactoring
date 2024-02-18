package store.mybooks.resource.order_detail_status.dto.response;

import org.mapstruct.Mapper;
import store.mybooks.resource.order_detail_status.entity.OrderDetailStatus;

/**
 * packageName    : store.mybooks.resource.order_detail_status.dto.response
 * fileName       : OrderDetailMapper
 * author         : minsu11
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        minsu11       최초 생성
 */
@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailStatusResponse mapToOrderDetailStatusResponse(OrderDetailStatus orderDetailStatus);
}
