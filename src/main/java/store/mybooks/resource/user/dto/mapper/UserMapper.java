package store.mybooks.resource.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserGradeModifyResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.dto.response.UserOauthCreateResponse;
import store.mybooks.resource.user.dto.response.UserStatusModifyResponse;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user.dto.mapper<br>
 * fileName       : UserCreateMapper<br>
 * author         : masiljangajji<br>
 * date           : 2/18/24<br>
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

    @Mapping(source = "userStatus.id", target = "userStatusName")
    @Mapping(source = "userGrade.userGradeName.id", target = "userGradeName")
    UserOauthCreateResponse toUserOauthCreateResponse(User user);

}
