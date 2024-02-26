package store.mybooks.resource.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserGradeModifyResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.dto.response.UserPasswordModifyResponse;
import store.mybooks.resource.user.dto.response.UserStatusModifyResponse;
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

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {


    @Mapping(source = "userStatus.id", target = "userStatusName")
    @Mapping(source = "userGrade.userGradeName.id", target = "userGradeName")
    UserCreateResponse toUserCreateResponse(User user);


    UserModifyResponse toUserModifyResponse(User user);

    @Mapping(source = "userGrade.userGradeName.id", target = "userGradeName")
    UserGradeModifyResponse toUserGradeModifyResponse(User user);
    @Mapping(source = "userStatus.id", target = "userStatusName")
    UserStatusModifyResponse toUserStatusModifyResponse(User user);

}
