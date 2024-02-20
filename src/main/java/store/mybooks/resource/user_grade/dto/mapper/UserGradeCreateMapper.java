package store.mybooks.resource.user_grade.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.user.dto.mapper.UserModifyMapper;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;

/**
 * packageName    : store.mybooks.resource.user_grade.dto.mapper
 * fileName       : UserGradeCreateMapper
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGradeCreateMapper {


    UserGradeCreateMapper INSTANCE = Mappers.getMapper(UserGradeCreateMapper.class);

    UserGradeCreateResponse toUserGradeCreateResponse(UserGrade userGrade);




}
