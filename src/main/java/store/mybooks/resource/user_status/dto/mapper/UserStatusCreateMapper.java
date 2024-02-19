package store.mybooks.resource.user_status.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.user_status.dto.response.UserStatusCreateResponse;
import store.mybooks.resource.user_status.entity.UserStatus;

/**
 * packageName    : store.mybooks.resource.user_status.dto.mapper
 * fileName       : UserStatusCreateMapper
 * author         : masiljangajji
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        masiljangajji       최초 생성
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserStatusCreateMapper {

    UserStatusCreateMapper INSTANCE = Mappers.getMapper(UserStatusCreateMapper.class);

    UserStatusCreateResponse toUserStatusCreateResponse(UserStatus userStatus);

}
