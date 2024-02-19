package store.mybooks.resource.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user.dto.response
 * fileName       : UserModifyMapper
 * author         : masiljangajji
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        masiljangajji       최초 생성
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserModifyMapper {

    UserModifyMapper INSTANCE = Mappers.getMapper(UserModifyMapper.class);

    @Mapping(source = "userStatus.id", target = "userStatusName")
    @Mapping(source = "userGrade.name", target = "userGradeName")
    UserModifyResponse toUserModifyResponse(User user);

}
