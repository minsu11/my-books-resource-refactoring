package store.mybooks.resource.user_grade.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;

/**
 * packageName    : store.mybooks.resource.user_grade.dto.mapper<br>
 * fileName       : UserGradeCreateMapper<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserGradeMapper {


    @Mapping(source = "userGradeName.id", target = "name")
    UserGradeCreateResponse toUserGradeCreateResponse(UserGrade userGrade);


}
