package store.mybooks.resource.cart.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.cart.entity.Cart;

/**
 * packageName    : store.mybooks.resource.cart.dto
 * fileName       : CartResponseMapper
 * author         : Fiat_lux
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        Fiat_lux       최초 생성
 */
@Mapper(componentModel = "spring")
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartResponse mapToResponse(Cart cart);
}
