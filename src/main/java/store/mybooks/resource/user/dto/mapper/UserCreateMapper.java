package store.mybooks.resource.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user.dto.mapper
 * fileName       : UserCreateMapper
 * author         : masiljangajji
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        masiljangajji       최초 생성
 */

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCreateMapper {

    // todo 이거 추후 삭제가능 , 혹은 변경가능
    UserCreateMapper INSTANCE = Mappers.getMapper(UserCreateMapper.class);

    UserCreateResponse toUserCreateResponse(User user);


}
