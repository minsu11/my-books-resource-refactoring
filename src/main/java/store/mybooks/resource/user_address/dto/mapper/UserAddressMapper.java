package store.mybooks.resource.user_address.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.user_address.dto.response.UserAddressCreateResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressModifyResponse;
import store.mybooks.resource.user_address.entity.UserAddress;

/**
 * packageName    : store.mybooks.resource.user_address.dto.mapper
 * fileName       : UserAddressMapper
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserAddressMapper {

    UserAddressMapper INSTANCE = Mappers.getMapper(UserAddressMapper.class);

    UserAddressCreateResponse toUserAddressCreateResponse(UserAddress userAddress);


    UserAddressModifyResponse toUserAddressModifyResponse(UserAddress userAddress);



}
